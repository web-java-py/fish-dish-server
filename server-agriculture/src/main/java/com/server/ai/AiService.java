package com.server.ai;

import com.server.properties.AiOllamaProperties;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

import static dev.langchain4j.data.message.UserMessage.userMessage;

@Component
public class AiService {

    private OllamaChatModel ollamaChatModel;
    private StreamingChatLanguageModel streamingChatLanguageModel;

    public AiService(AiOllamaProperties aiOllamaProperties) {
        ollamaChatModel = OllamaChatModel.builder()
                .baseUrl(aiOllamaProperties.getBaseUrl())
                .modelName(aiOllamaProperties.getModelName())
                .temperature(aiOllamaProperties.getTemperature())
                .timeout(Duration.ofSeconds(aiOllamaProperties.getTimeout()))
                .build();
        streamingChatLanguageModel = OllamaStreamingChatModel.builder()
                .baseUrl(aiOllamaProperties.getBaseUrl())
                .modelName(aiOllamaProperties.getModelName())
                .temperature(aiOllamaProperties.getTemperature())
                .build();
    }

    public String chat(String prompt) {
        return ollamaChatModel.chat(prompt);
    }

    public String chat(String prompt, ResponseFormat responseFormat) {
        ChatResponse chatResponse = ollamaChatModel.chat(ChatRequest.builder()
                .messages(userMessage(prompt))
                .responseFormat(responseFormat)
                .build());
        return chatResponse.aiMessage().text();
    }

    public Flux<String> chatStream(String prompt) {
        return Flux.create(sink -> {
            streamingChatLanguageModel.chat(prompt, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    sink.next(partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    sink.complete();
                }

                @Override
                public void onError(Throwable error) {
                    sink.error(error);
                }
            });
        });
    }
}
