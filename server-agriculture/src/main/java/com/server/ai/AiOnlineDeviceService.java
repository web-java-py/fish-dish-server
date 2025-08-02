package com.server.ai;

import com.server.ai.sql.SqlDatabaseContentRetriever;
import com.server.ai.tool.AgricultureTools;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agent.tool.ToolSpecifications;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.aggregator.ContentAggregator;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.rag.query.router.QueryRouter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static dev.langchain4j.data.message.UserMessage.userMessage;

@Slf4j
@Service
public class AiOnlineDeviceService {

    @Autowired
    public OllamaChatModel onlineModel;
    @Autowired
    private AgricultureTools agricultureTools;
    @Autowired
    private QueryRouter queryRouter;
    @Autowired
    private ContentAggregator contentAggregator;
    @Autowired
    private ContentInjector contentInjector;

    private static final Prompt DEFAULT_PROMPT = Prompt.from(
            "You are an expert in observing IoT devices, and your main responsibility is to comprehensively monitor and manage the operational status of various IoT devices. \n" +
                    "By analyzing the monitoring data of the equipment in real-time, you can quickly identify and adjust the operating thresholds of the equipment to ensure that they are always in optimal working condition. \n" +
                    "When the operating status of a device exceeds the preset range, you will immediately take corresponding measures and adjust the appropriate response device. \n" +
                    "At the same time, after the device status returns to normal, you will promptly shut down the relevant adjustment devices to maintain the efficiency and security of the system. \n" +
                    "You have solid technical knowledge and keen observation skills, which can effectively prevent and solve possible problems, ensuring the stable operation of IoT devices."
    );

    public void online(String prompt) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(DEFAULT_PROMPT.toSystemMessage());
        chatMessages.add(CreateRagChatMessage(prompt));
        ChatResponse chatResponse = onlineModel.chat(getChatRequestTool(chatMessages));
        List<ToolExecutionRequest> toolExecutionRequests = chatResponse.aiMessage().toolExecutionRequests();
        if (ObjectUtils.isNotEmpty(toolExecutionRequests)) {
            chatMessages.add(chatResponse.aiMessage());
            List<ChatMessage> chatMessageList = AiUtils.operateToolExecution(agricultureTools, toolExecutionRequests, chatMessages);
            log.info(onlineModel.chat(getChatRequestTool(chatMessageList)).aiMessage().text());
        } else {
            log.info(chatResponse.aiMessage().text());
        }
    }

    private ChatRequest getChatRequestTool(List<ChatMessage> messages) {
        List<ToolSpecification> toolSpecifications = ToolSpecifications.toolSpecificationsFrom(agricultureTools);
        return ChatRequest.builder()
                .messages(messages)
                .toolSpecifications(toolSpecifications)
                .build();
    }

    public ChatMessage CreateRagChatMessage(String prompt) {
        Query query = Query.from(prompt);
        Map<Query, Collection<List<Content>>> queryCollectionMap = new HashMap<>();
        Collection<ContentRetriever> contentRetrievers = queryRouter.route(query);
        List<List<Content>> contents = new ArrayList<>();
        for (ContentRetriever contentRetriever : contentRetrievers) {
            contents.add(contentRetriever.retrieve(query));
        }
        queryCollectionMap.put(query, contents);
        List<Content> finalContents = contentAggregator.aggregate(queryCollectionMap);
        return contentInjector.inject(finalContents, userMessage(prompt));
    }
}
