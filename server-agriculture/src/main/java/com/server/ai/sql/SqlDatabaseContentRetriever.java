package com.server.ai.sql;

import com.server.ai.AiUtils;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.lang3.ObjectUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.internal.Utils.getOrDefault;
import static dev.langchain4j.internal.ValidationUtils.ensureNotNull;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public class SqlDatabaseContentRetriever implements ContentRetriever {
    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = PromptTemplate.from(
            "You are an expert in writing SQL queries.\n" +
                    "You have access to a {{sqlDialect}} database with the following structure:\n" +
                    "{{databaseStructure}}\n" +
                    "If a user asks a question that can be answered by querying this database, generate an SQL SELECT query.\n" +
                    "Do not output anything else aside from a valid SQL statement!"
    );

    private final DataSource dataSource;
    private final String sqlDialect;
    private final String databaseStructure;
    private final String[] needTables;

    private final PromptTemplate promptTemplate;
    private final ChatLanguageModel chatLanguageModel;

    private final int maxRetries;

    public SqlDatabaseContentRetriever(DataSource dataSource,
                                       String sqlDialect,
                                       String databaseStructure,
                                       PromptTemplate promptTemplate,
                                       ChatLanguageModel chatLanguageModel,
                                       Integer maxRetries,
                                       String[] needTables) {
        this.needTables = getOrDefault(needTables, new String[]{});
        this.dataSource = ensureNotNull(dataSource, "dataSource");
        this.sqlDialect = getOrDefault(sqlDialect, () -> getSqlDialect(dataSource));
        this.databaseStructure = getOrDefault(databaseStructure, () -> generateDDL(dataSource));
        this.promptTemplate = getOrDefault(promptTemplate, DEFAULT_PROMPT_TEMPLATE);
        this.chatLanguageModel = ensureNotNull(chatLanguageModel, "chatLanguageModel");
        this.maxRetries = getOrDefault(maxRetries, 1);
    }

    public static String getSqlDialect(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            return metaData.getDatabaseProductName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateDDL(DataSource dataSource) {
        StringBuilder ddl = new StringBuilder();

        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (ObjectUtils.isEmpty(needTables)){
                    String createTableStatement = generateCreateTableStatement(tableName, metaData);
                    ddl.append(createTableStatement).append("\n");
                    continue;
                }
                for (String needTable : needTables) {
                    if (tableName.equals(needTable)) {
                        String createTableStatement = generateCreateTableStatement(tableName, metaData);
                        ddl.append(createTableStatement).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ddl.toString();
    }

    private static String generateCreateTableStatement(String tableName, DatabaseMetaData metaData) {
        StringBuilder createTableStatement = new StringBuilder();

        try {
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            ResultSet pk = metaData.getPrimaryKeys(null, null, tableName);
            ResultSet fks = metaData.getImportedKeys(null, null, tableName);

            String primaryKeyColumn = "";
            if (pk.next()) {
                primaryKeyColumn = pk.getString("COLUMN_NAME");
            }

            createTableStatement
                    .append("CREATE TABLE ")
                    .append(tableName)
                    .append(" (\n");

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                int size = columns.getInt("COLUMN_SIZE");
                String nullable = columns.getString("IS_NULLABLE").equals("YES") ? " NULL" : " NOT NULL";
                String columnDef = columns.getString("COLUMN_DEF") != null ? " DEFAULT " + columns.getString("COLUMN_DEF") : "";
                String comment = columns.getString("REMARKS");

                createTableStatement
                        .append("  ")
                        .append(columnName)
                        .append(" ")
                        .append(columnType)
                        .append("(")
                        .append(size)
                        .append(")")
                        .append(nullable)
                        .append(columnDef);

                if (columnName.equals(primaryKeyColumn)) {
                    createTableStatement.append(" PRIMARY KEY");
                }

                createTableStatement.append(",\n");

                if (comment != null && !comment.isEmpty()) {
                    createTableStatement
                            .append("  COMMENT ON COLUMN ")
                            .append(tableName)
                            .append(".")
                            .append(columnName)
                            .append(" IS '")
                            .append(comment)
                            .append("',\n");
                }
            }

            while (fks.next()) {
                String fkColumnName = fks.getString("FKCOLUMN_NAME");
                String pkTableName = fks.getString("PKTABLE_NAME");
                String pkColumnName = fks.getString("PKCOLUMN_NAME");
                createTableStatement
                        .append("  FOREIGN KEY (")
                        .append(fkColumnName)
                        .append(") REFERENCES ")
                        .append(pkTableName)
                        .append("(")
                        .append(pkColumnName)
                        .append("),\n");
            }

            if (createTableStatement.charAt(createTableStatement.length() - 2) == ',') {
                createTableStatement.delete(createTableStatement.length() - 2, createTableStatement.length());
            }

            createTableStatement.append(");\n");

            ResultSet tableRemarks = metaData.getTables(null, null, tableName, null);
            if (tableRemarks.next()) {
                String tableComment = tableRemarks.getString("REMARKS");
                if (tableComment != null && !tableComment.isEmpty()) {
                    createTableStatement
                            .append("COMMENT ON TABLE ")
                            .append(tableName)
                            .append(" IS '")
                            .append(tableComment)
                            .append("';\n");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return createTableStatement.toString();
    }

    public static SqlDatabaseContentRetrieverBuilder builder() {
        return new SqlDatabaseContentRetrieverBuilder();
    }

    @Override
    public List<Content> retrieve(Query naturalLanguageQuery) {
        String sqlQuery = null;
        String errorMessage = null;

        int attemptsLeft = maxRetries + 1;
        while (attemptsLeft > 0) {
            attemptsLeft--;

            sqlQuery = generateSqlQuery(naturalLanguageQuery, sqlQuery, errorMessage);

            sqlQuery = clean(sqlQuery);

            if (!isSelect(sqlQuery)) {
                return emptyList();
            }

            try {
                validate(sqlQuery);

                try (Connection connection = dataSource.getConnection();
                     Statement statement = connection.createStatement()) {

                    String result = execute(sqlQuery, statement);
                    Content content = format(result, sqlQuery);
                    return singletonList(content);
                }
            } catch (Exception e) {
                errorMessage = e.getMessage();
            }
        }

        return emptyList();
    }

    protected String generateSqlQuery(Query naturalLanguageQuery, String previousSqlQuery, String previousErrorMessage) {

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(createSystemPrompt().toSystemMessage());
        messages.add(UserMessage.from(naturalLanguageQuery.text()));

        if (previousSqlQuery != null && previousErrorMessage != null) {
            messages.add(AiMessage.from(previousSqlQuery));
            messages.add(UserMessage.from(previousErrorMessage));
        }

        return chatLanguageModel.chat(messages).aiMessage().text();
    }

    protected Prompt createSystemPrompt() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("sqlDialect", sqlDialect);
        variables.put("databaseStructure", databaseStructure);
        return promptTemplate.apply(variables);
    }

    protected String clean(String sqlQuery) {
        sqlQuery = AiUtils.cleanThink(sqlQuery);
        if (sqlQuery.contains("```sql")) {
            return sqlQuery.substring(sqlQuery.indexOf("```sql") + 6, sqlQuery.lastIndexOf("```"));
        } else if (sqlQuery.contains("```")) {
            return sqlQuery.substring(sqlQuery.indexOf("```") + 3, sqlQuery.lastIndexOf("```"));
        }
        return sqlQuery;
    }

    protected void validate(String sqlQuery) {

    }

    protected boolean isSelect(String sqlQuery) {
        try {
            net.sf.jsqlparser.statement.Statement statement = CCJSqlParserUtil.parse(sqlQuery);
            return statement instanceof Select;
        } catch (JSQLParserException e) {
            return false;
        }
    }

    protected String execute(String sqlQuery, Statement statement) throws SQLException {
        List<String> resultRows = new ArrayList<>();

        try (ResultSet resultSet = statement.executeQuery(sqlQuery)) {
            int columnCount = resultSet.getMetaData().getColumnCount();

            // header
            List<String> columnNames = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(resultSet.getMetaData().getColumnName(i));
            }
            resultRows.add(String.join(",", columnNames));

            // rows
            while (resultSet.next()) {
                List<String> columnValues = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {

                    String columnValue = resultSet.getObject(i) == null ? "" : resultSet.getObject(i).toString();

                    if (columnValue.contains(",")) {
                        columnValue = "\"" + columnValue + "\"";
                    }
                    columnValues.add(columnValue);
                }
                resultRows.add(String.join(",", columnValues));
            }
        }

        return String.join("\n", resultRows);
    }

    private static Content format(String result, String sqlQuery) {
        return Content.from(String.format("Result of executing '%s':\n%s", sqlQuery, result));
    }

    public static class SqlDatabaseContentRetrieverBuilder {
        private DataSource dataSource;
        private String sqlDialect;
        private String databaseStructure;
        private PromptTemplate promptTemplate;
        private ChatLanguageModel chatLanguageModel;
        private Integer maxRetries;
        private String[] needTables;

        SqlDatabaseContentRetrieverBuilder() {
        }

        public SqlDatabaseContentRetrieverBuilder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public SqlDatabaseContentRetrieverBuilder sqlDialect(String sqlDialect) {
            this.sqlDialect = sqlDialect;
            return this;
        }

        public SqlDatabaseContentRetrieverBuilder databaseStructure(String databaseStructure) {
            this.databaseStructure = databaseStructure;
            return this;
        }

        public SqlDatabaseContentRetrieverBuilder promptTemplate(PromptTemplate promptTemplate) {
            this.promptTemplate = promptTemplate;
            return this;
        }

        public SqlDatabaseContentRetrieverBuilder chatLanguageModel(ChatLanguageModel chatLanguageModel) {
            this.chatLanguageModel = chatLanguageModel;
            return this;
        }

        public SqlDatabaseContentRetrieverBuilder needTables(String[] needTables) {
            this.needTables = needTables;
            return this;
        }

        public SqlDatabaseContentRetrieverBuilder maxRetries(Integer maxRetries) {
            this.maxRetries = maxRetries;
            return this;
        }

        public SqlDatabaseContentRetriever build() {
            return new SqlDatabaseContentRetriever(this.dataSource, this.sqlDialect, this.databaseStructure, this.promptTemplate, this.chatLanguageModel, this.maxRetries, this.needTables);
        }

        public String toString() {
            return "SqlDatabaseContentRetriever.SqlDatabaseContentRetrieverBuilder(dataSource=" + this.dataSource + ", sqlDialect=" + this.sqlDialect + ", databaseStructure=" + this.databaseStructure + ", promptTemplate=" + this.promptTemplate + ", chatLanguageModel=" + this.chatLanguageModel + ", maxRetries=" + this.maxRetries + ")";
        }
    }
}
