package com.server.ai;

import com.server.enums.ClassType;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.*;

import java.io.InputStream;

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

    public static ResponseFormat classReportResponseFormat(ClassType classType) {
        JsonObjectSchema environmentalParamsSchema;
        if (classType == ClassType.FISH) {
            environmentalParamsSchema = JsonObjectSchema.builder()
                    .addProperty("water_temperature", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("ph", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("dissolved_oxygen", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("ammonia_nitrogen", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("nitrite", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .build();
        } else {
            environmentalParamsSchema = JsonObjectSchema.builder()
                    .addProperty("temperature", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("humidity", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("light", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("wind_direction", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("wind_speed", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .addProperty("ph", JsonArraySchema.builder()
                            .items(JsonStringSchema.builder().build())
                            .build())
                    .build();
        }

        // 创建建议模式
        JsonObjectSchema suggestionSchema = JsonObjectSchema.builder()
                .addProperty("type", JsonStringSchema.builder().build())
                .addProperty("content", JsonStringSchema.builder().build())
                .build();

        // 创建核心指标模式
        JsonObjectSchema coreIndicatorsSchema = JsonObjectSchema.builder()
                .addProperty("growth_rate", JsonIntegerSchema.builder().build())
                .addProperty("disease_resistance", JsonIntegerSchema.builder().build())
                .addProperty((ClassType.FISH == classType ? "feed" : "fertilizer") + "_conversion", JsonIntegerSchema.builder().build())
                .addProperty("market_acceptance", JsonIntegerSchema.builder().build())
                .build();

        // 创建综合评估模式
        JsonObjectSchema comprehensiveAssessmentSchema = JsonObjectSchema.builder()
                .addProperty("growth_assessment", JsonStringSchema.builder().build())
                .addProperty((ClassType.FISH == classType ? "breeding" : "cultivation") + "_difficulty", JsonStringSchema.builder().build())
                .addProperty("general_recommendations", JsonStringSchema.builder().build())
                .addProperty("market_analysis", JsonStringSchema.builder().build())
                .build();

        String suggestions = (ClassType.FISH == classType ? "breeding" : "planting") + "_suggestions";

        // 构建根对象模式
        JsonObjectSchema rootSchema = JsonObjectSchema.builder()
                .addProperty("environmental_params", environmentalParamsSchema)
                .addProperty(suggestions, JsonArraySchema.builder()
                        .items(suggestionSchema)
                        .build())
                .addProperty("core_indicators", coreIndicatorsSchema)
                .addProperty("comprehensive_assessment", comprehensiveAssessmentSchema)
                .required("environmental_params", suggestions, "core_indicators", "comprehensive_assessment")
                .build();

        // 构建最终响应格式
        return ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .rootElement(rootSchema)
                        .build())
                .build();
    }
}
