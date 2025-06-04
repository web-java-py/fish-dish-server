package com.server.ai;

import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.*;

public class AiResponseFormat {

    public static ResponseFormat jobResponseFormat() {
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder().rootElement(JsonObjectSchema.builder()
                                .addProperty("species", JsonStringSchema.builder().build())
                                .addProperty("jobs", JsonArraySchema.builder()
                                        .items(JsonObjectSchema.builder()
                                                .addProperty("jobName", JsonStringSchema.builder().build())
                                                .addProperty("cycUnit", JsonIntegerSchema.builder().build())
                                                .addProperty("jobStart", JsonIntegerSchema.builder().build())
                                                .addProperty("jobFinish", JsonIntegerSchema.builder().build())
                                                .required("jobName", "cycUnit", "jobStart", "jobFinish")
                                                .build())
                                        .build())
                                .required("species", "jobs")
                                .build())
                        .build())
                .build();
    }
}
