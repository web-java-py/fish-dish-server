package com.server.ai.tool;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

@Component
public class AgricultureTools {
    @Tool("启动推杆")
    void startPushRod() {
        System.out.println("666666666666666666666666");
    }
    @Tool("关闭推杆")
    void stopPushRod() {
        System.out.println("666666666666666666666666");
    }
}
