package com.server.ai;

import com.alibaba.fastjson2.JSON;
import com.server.ai.sql.SqlDatabaseContentRetriever;
import com.server.ai.tool.AgricultureTools;
import com.server.constant.AiConstants;
import com.server.domain.dto.IdentifyDTO;
import com.server.domain.vo.ChatStreamVO;
import com.server.exception.ServiceException;
import com.server.properties.AiLocalProperties;
import com.server.utils.HttpUtils;
import com.server.utils.StringUtils;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
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
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static dev.langchain4j.data.message.SystemMessage.systemMessage;
import static dev.langchain4j.data.message.UserMessage.userMessage;

@Slf4j
@Service
public class AiService {

    @Autowired
    private AiLocalProperties aiLocalProperties;
    @Autowired
    private AgricultureTools agricultureTools;
    @Autowired
    public OllamaChatModel ollamaChatModel;
    @Autowired
    private StreamingChatLanguageModel streamingChatLanguageModel;
    @Autowired
    private QueryRouter queryRouter;
    @Autowired
    private ContentAggregator contentAggregator;
    @Autowired
    private ContentInjector contentInjector;
    @Autowired
    private Boolean AiThink;
    @Autowired
    public OllamaChatModel onlineModel;

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
                log.error(e.getMessage());
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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(AiConstants.AGRICULTURE_BOT);
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(systemMessage(new TextDocumentParser().parse(inputStream).text()));
        chatMessages.add(CreateRagChatMessage(prompt, AiConstants.AGRICULTURE_BOT_NAME));
        return Flux.create(sink -> {
            streamingChatLanguageModel.chat(getChatRequestTool(chatMessages), new StreamingChatResponseHandler() {

                @Override
                public void onPartialResponse(String partialResponse) {
                    sinkNext(sink, partialResponse);
                }

                @Override
                public void onCompleteResponse(ChatResponse completeResponse) {
                    List<ToolExecutionRequest> toolExecutionRequests = completeResponse.aiMessage().toolExecutionRequests();
                    if (ObjectUtils.isNotEmpty(toolExecutionRequests)) {
                        chatMessages.add(completeResponse.aiMessage());
                        List<ChatMessage> chatMessageList = AiUtils.operateToolExecution(agricultureTools, toolExecutionRequests, chatMessages, AiConstants.AGRICULTURE_BOT_NAME);
                        streamingChatLanguageModel.chat(getChatRequestTool(chatMessageList), new StreamingChatResponseHandler() {

                            @Override
                            public void onPartialResponse(String partialResponse) {
                                sinkNext(sink, partialResponse);
                            }

                            @Override
                            public void onCompleteResponse(ChatResponse chatResponse) {
                                sink.complete();
                            }

                            @Override
                            public void onError(Throwable error) {
                                sink.error(error);
                            }
                        });
                    } else {
                        sink.complete();
                    }
                }

                @Override
                public void onError(Throwable error) {
                    sink.error(error);
                }
            });
        });
    }

    public void onlineAgents(String prompt) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(AiConstants.ASH_BOT.toSystemMessage());
        chatMessages.add(CreateRagChatMessage(prompt, AiConstants.ASH_BOT_NAME));
        ChatResponse chatResponse = onlineModel.chat(getChatRequestTool(chatMessages));
        List<ToolExecutionRequest> toolExecutionRequests = chatResponse.aiMessage().toolExecutionRequests();
        if (ObjectUtils.isNotEmpty(toolExecutionRequests)) {
            chatMessages.add(chatResponse.aiMessage());
            List<ChatMessage> chatMessageList = AiUtils.operateToolExecution(agricultureTools, toolExecutionRequests, chatMessages, AiConstants.ASH_BOT_NAME);
            log.info("{}实时监听心跳:" + AiUtils.cleanThinkRe(onlineModel.chat(getChatRequestTool(chatMessageList)).aiMessage().text()), AiConstants.ASH_BOT_NAME);
        } else {
            log.info("{}实时监听心跳:" + AiUtils.cleanThinkRe(chatResponse.aiMessage().text()), AiConstants.ASH_BOT_NAME);
        }
    }

    public ChatMessage CreateRagChatMessage(String prompt, String bot) {
        Query query = Query.from(AiUtils.setThink(prompt));
        Map<Query, Collection<List<Content>>> queryCollectionMap = new HashMap<>();
        Collection<ContentRetriever> contentRetrievers = queryRouter.route(query);
        List<List<Content>> contents = new ArrayList<>();
        for (ContentRetriever contentRetriever : contentRetrievers) {
            if (bot.equals(AiConstants.ASH_BOT_NAME) && contentRetriever instanceof EmbeddingStoreContentRetriever) continue;
            contents.add(contentRetriever.retrieve(query));
        }
        queryCollectionMap.put(query, contents);
        List<Content> finalContents = contentAggregator.aggregate(queryCollectionMap);
        return contentInjector.inject(finalContents, userMessage(query.text()));
    }

    private void sinkNext(FluxSink<String> sink, String partialResponse) {
        String response = AiUtils.cleanThink(AiThink, partialResponse);
        if (!AiUtils.thinkOFF()) return;
        sink.next(JSON.toJSONString(ChatStreamVO.builder()
                .response(response)
                .build()));
    }

    private ChatRequest getChatRequestTool(List<ChatMessage> messages) {
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(agricultureTools);
        return ChatRequest.builder()
                .messages(messages)
                .toolSpecifications(toolSpecifications)
                .build();
    }
}
