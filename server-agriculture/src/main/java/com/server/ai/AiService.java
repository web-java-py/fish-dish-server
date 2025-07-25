package com.server.ai;

import com.alibaba.fastjson2.JSON;
import com.server.ai.tool.AgricultureTools;
import com.server.constant.AiConstants;
import com.server.domain.dto.IdentifyDTO;
import com.server.domain.vo.ChatStreamVO;
import com.server.exception.ServiceException;
import com.server.properties.AiLocalProperties;
import com.server.properties.AiOllamaProperties;
import com.server.utils.HttpUtils;
import com.server.utils.StringUtils;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.message.*;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import lombok.extern.java.Log;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;

@Log
@Component
public class AiService {

    @Autowired
    private AiLocalProperties aiLocalProperties;
    @Autowired
    private AgricultureTools agricultureTools;

    public OllamaChatModel ollamaChatModel;
    private StreamingChatLanguageModel streamingChatLanguageModel;
    private String think;

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
        this.think = aiOllamaProperties.getThink() ? "/set think" : "/set nothink";
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

    public IdentifyDTO identify(String prompt, String file) {
        if (StringUtils.isEmpty(file)) {
            throw new ServiceException("请上传识别图像！");
        }
        Map paramMap = new HashMap<String, Object>();
        paramMap.put("prompt_text", prompt);
        String response = HttpUtils.doPost4Json(aiLocalProperties.getVlUrl() + "/identify", paramMap, file);
        return JSON.parseObject(response, IdentifyDTO.class);
    }

    public Flux<String> chatVl(String prompt, String file) {
        Map paramMap = new HashMap<String, Object>();
        paramMap.put("prompt_text", prompt);
        return Flux.create(sink -> {
            CloseableHttpResponse response = HttpUtils.doPostStream(aiLocalProperties.getVlUrl() + "/chat", paramMap, file);
            try (InputStream inputStream = response.getEntity().getContent();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals("")) sink.next(line);
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

    public Flux<String> chatAgentsStream(String prompt) {
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(agricultureTools);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(AiConstants.BOT);
        SystemMessage systemMessage = systemMessage(new TextDocumentParser().parse(inputStream).text());
        UserMessage userMessage = userMessage(prompt + this.think);
        ChatResponse chat = ollamaChatModel.chat(ChatRequest.builder()
                .messages(systemMessage, userMessage)
                .toolSpecifications(toolSpecifications)
                .build());
        AiMessage aiMessage = chat.aiMessage();
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(systemMessage);
        chatMessages.add(userMessage);
        chatMessages.add(aiMessage);
        List<ChatMessage> chatMessageList = AiUtils.operateToolExecution(agricultureTools, aiMessage.toolExecutionRequests(), chatMessages);
        return Flux.create(sink -> {
            streamingChatLanguageModel.chat(ChatRequest.builder()
                    .messages(chatMessageList)
                    .toolSpecifications(toolSpecifications)
                    .build(), new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String partialResponse) {
                    String response = partialResponse.replace("<think>", "").replace("</think>", "");
                    if (StringUtils.isEmpty(response)) return;
                    sink.next(JSON.toJSONString(ChatStreamVO.builder()
                            .response(response)
                            .build()));
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
