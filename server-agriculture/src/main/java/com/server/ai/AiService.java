package com.server.ai;

import com.server.exception.ServiceException;
import com.server.properties.AiLocalProperties;
import com.server.properties.AiOllamaProperties;
import com.server.utils.HttpUtils;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import lombok.extern.java.Log;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static dev.langchain4j.data.message.UserMessage.userMessage;

@Log
@Component
public class AiService {

    private AiLocalProperties aiLocalProperties;

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

    public String identify(String prompt, MultipartFile file) {
        if (ObjectUtils.isEmpty(file) || file.isEmpty()){
            throw new ServiceException("请上传识别图像！");
        }
        Map paramMap = new HashMap<String, Object>();
        paramMap.put("prompt_text", prompt);
        return HttpUtils.doPost4Json(aiLocalProperties.getVlUrl() + "/identify", paramMap, file);
    }

    public Flux<String> chatVl(String prompt, MultipartFile file) {
        Map paramMap = new HashMap<String, Object>();
        paramMap.put("prompt_text", prompt);
        CloseableHttpResponse response = HttpUtils.doPostStream(aiLocalProperties.getVlUrl() + "/chat", paramMap, file);
        return Flux.create(sink -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sink.next(line);
                }
                sink.complete();
            } catch (Exception e) {
                sink.error(e);
                log.warning(e.getMessage());
            } finally {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
