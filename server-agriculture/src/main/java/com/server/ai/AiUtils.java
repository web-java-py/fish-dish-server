package com.server.ai;

import com.server.ai.tool.AgricultureTools;
import com.server.utils.StringUtils;
import dev.langchain4j.agent.tool.ToolExecutionRequest;
import dev.langchain4j.data.message.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import static dev.langchain4j.data.message.ToolExecutionResultMessage.toolExecutionResultMessage;

public class AiUtils {

    private static final Logger log = LoggerFactory.getLogger(AiUtils.class);

    private static int thinkCount = 0;

    private static boolean thinkOFF = false;

    public static List<ChatMessage> operateToolExecution(AgricultureTools agricultureTools, List<ToolExecutionRequest> toolExecutionRequests, List<ChatMessage> chatMessages, String bot) {
        if (toolExecutionRequests != null && toolExecutionRequests.size() > 0) {
            toolExecutionRequests.forEach(toolExecutionRequest -> {
                String methodName = toolExecutionRequest.name(); // 获取请求的工具名称
                try {
                    // 获取方法的参数类型
                    Method method = agricultureTools.getClass().getDeclaredMethod(methodName, String.class);
                    method.invoke(agricultureTools, bot); // 调用方法
                    chatMessages.add(toolExecutionResultMessage(toolExecutionRequest, "true"));
                } catch (NoSuchMethodException e) {
                    log.error("方法 " + methodName + " 不存在: " + e.getMessage());
                    chatMessages.add(toolExecutionResultMessage(toolExecutionRequest, "false - " + e.getMessage()));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    log.error("调用方法 " + methodName + " 时出现错误: " + e.getCause());
                    chatMessages.add(toolExecutionResultMessage(toolExecutionRequest, "false - " + e.getCause()));
                }
            });
        }
        return chatMessages;
    }

    public static String setThink(String prompt) {
        return prompt + "/set nothink";
    }

    public static String setThink(boolean AiThink, String prompt) {
        return AiThink ? prompt + "/set think" : prompt + "/set nothink";
    }

    public static String setThink(boolean AiThink) {
        return AiThink ? "/set think" : "/set nothink";
    }

    public static String cleanThink(boolean AiThink, String think) {
        thinkHandle(think);
        return AiThink ? think : think.replace("<think>", "").replace("</think>", "");
    }

    public static String cleanThinkRe(boolean AiThink, String think) {
        return AiThink ? think : think.replaceAll("<think>.*?</think>", "");
    }

    public static String cleanThinkRe(String think) {
        return think.replaceAll("<think>\n\n</think>\n\n", "");
    }

    public static String cleanThink(String think) {
        return think.replace("<think>", "").replace("</think>", "");
    }

    public static void thinkHandle(String think) {
        boolean t = think.equals("<think>") || think.equals("</think>");
        if (t) {
            thinkCount++;
            thinkOFF = false;
        } else if (!think.equals("") && !think.contains("\n") && (thinkCount == 2 || thinkCount == 4)) {
            thinkOFF = true;
            thinkCount = 0;
        }
    }

    public static boolean thinkOFF() {
        return thinkOFF;
    }
}
