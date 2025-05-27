package com.server.ai;

import com.server.properties.AIOllamaProperties;
import dev.langchain4j.model.ollama.OllamaChatModel;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AIService {

    private OllamaChatModel ollamaChatModel;

    public AIService(AIOllamaProperties aiOllamaProperties) {
        System.out.println(aiOllamaProperties);
        this.ollamaChatModel = OllamaChatModel.builder()
                .baseUrl(aiOllamaProperties.getBaseUrl())
                .modelName(aiOllamaProperties.getModelName())
                .temperature(aiOllamaProperties.getTemperature())
                .timeout(Duration.ofSeconds(aiOllamaProperties.getTimeout()))
                .build();
    }

    public String chat(String Prompt) {
        return ollamaChatModel.chat(Prompt);
    }
}
