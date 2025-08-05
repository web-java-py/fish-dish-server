package com.server.config;

import com.server.ai.AiUtils;
import com.server.ai.sql.SqlDatabaseContentRetriever;
import com.server.properties.AiOllamaProperties;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.aggregator.DefaultContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.router.DefaultQueryRouter;
import dev.langchain4j.rag.query.router.QueryRouter;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Configuration
public class AiAgentConfiguration {

    @Autowired
    private AiOllamaProperties aiOllamaProperties;

    @Bean
    public OllamaChatModel ollamaChatModel() {
        return OllamaChatModel.builder()
                .baseUrl(aiOllamaProperties.getBaseUrl())
                .modelName(aiOllamaProperties.getModelName())
                .temperature(aiOllamaProperties.getTemperature())
                .timeout(Duration.ofSeconds(aiOllamaProperties.getTimeout()))
                .build();
    }

    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return OllamaStreamingChatModel.builder()
                .baseUrl(aiOllamaProperties.getBaseUrl())
                .modelName(aiOllamaProperties.getModelName())
                .temperature(aiOllamaProperties.getTemperature())
                .timeout(Duration.ofSeconds(aiOllamaProperties.getTimeout()))
                .build();
    }

    @Bean
    public OllamaChatModel onlineModel() {
        return OllamaChatModel.builder()
                .baseUrl(aiOllamaProperties.getOnlineUrl())
                .modelName(aiOllamaProperties.getOnlineModel())
                .temperature(aiOllamaProperties.getTemperature())
                .timeout(Duration.ofSeconds(aiOllamaProperties.getTimeout()))
                .build();
    }

    @Bean
    public EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl(aiOllamaProperties.getBaseUrl())
                .modelName(aiOllamaProperties.getModelName())
                .timeout(Duration.ofSeconds(aiOllamaProperties.getTimeout()))
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) throws IOException {
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        Resource resource = resourceLoader.getResource("classpath:templates/ai/terms-of-use.txt");
        Document document = loadDocument(resource.getFile().toPath(), new TextDocumentParser());

        DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 0);
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        embeddingStoreIngestor.ingest(document);

        return embeddingStore;
    }

    @Bean
    public ContentRetriever contentRetrieverDevice(OllamaChatModel ollamaChatModel, DataSource masterDataSource) {
        return SqlDatabaseContentRetriever.builder()
                .dataSource(masterDataSource)
                .chatLanguageModel(ollamaChatModel)
                .needTables(new String[]{"agriculture_threshold_config"})
                .build();
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .minScore(0.75)
                .build();
    }

    @Bean
    public QueryRouter queryRouter(ContentRetriever contentRetriever, ContentRetriever contentRetrieverDevice) {
        return new DefaultQueryRouter(contentRetriever, contentRetrieverDevice);
    }

    @Bean
    public ContentAggregator contentAggregator() {
        return new DefaultContentAggregator();
    }

    @Bean
    public ContentInjector contentInjector() {
        return DefaultContentInjector.builder()
                .promptTemplate(PromptTemplate.from("{{userMessage}}\n" + "使用以下信息回答：" + "\n{{contents}}\n" + AiUtils.setThink(aiOllamaProperties.getThink())))
                .build();
    }

    @Bean
    public RetrievalAugmentor retrievalAugmentor(QueryRouter queryRouter, ContentAggregator contentAggregator, ContentInjector contentInjector, ExecutorService AiRetrievalExecutorService) {
        return DefaultRetrievalAugmentor.builder()
                .queryRouter(queryRouter)
                .contentAggregator(contentAggregator)
                .contentInjector(contentInjector)
                .executor(AiRetrievalExecutorService)
                .build();
    }

    @Bean
    public Boolean AiThink() {
        return aiOllamaProperties.getThink();
    }
}
