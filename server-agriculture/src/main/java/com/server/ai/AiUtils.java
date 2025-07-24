package com.server.ai;

import com.server.ai.tool.AgricultureTools;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import static dev.langchain4j.data.message.ToolExecutionResultMessage.toolExecutionResultMessage;

public class AiUtils {

    private static final Logger log = LoggerFactory.getLogger(AiUtils.class);

    public static List<ChatMessage> operateToolExecution(AgricultureTools agricultureTools, List<ToolExecutionRequest> toolExecutionRequests, List<ChatMessage> chatMessages) {
        if (toolExecutionRequests != null && toolExecutionRequests.size() > 0) {
            toolExecutionRequests.forEach(toolExecutionRequest -> {
                String methodName = toolExecutionRequest.name(); // 获取请求的工具名称
                try {
                    // 获取方法的参数类型
                    Method method = agricultureTools.getClass().getDeclaredMethod(methodName);
                    // 如果方法存在且是非私有的，调用该方法
                    if (method != null) {
                        method.setAccessible(true); // 设置为可访问，如果是私有方法
                        method.invoke(agricultureTools); // 调用方法
                    }
                    chatMessages.add(toolExecutionResultMessage(toolExecutionRequest, "true"));
                } catch (NoSuchMethodException e) {
                    log.error("方法 " + methodName + " 不存在: " + e.getMessage());
                    chatMessages.add(toolExecutionResultMessage(toolExecutionRequest, "false - " + e.getMessage()));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("调用方法 " + methodName + " 时出现错误: " + e.getMessage());
                    chatMessages.add(toolExecutionResultMessage(toolExecutionRequest, "false - " + e.getMessage()));
                }
            });
        }
        return chatMessages;
    }
}
