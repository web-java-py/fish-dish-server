package com.server.ai.tool;

import dev.langchain4j.agent.tool.Tool;

public class AgricultureTools {
    @Tool("对给定的 2 个数字求和")
    double sum(double a, double b) {
        return a + b;
    }

    @Tool("返回给定数字的平方根")
    double squareRoot(double x) {
        return Math.sqrt(x);
    }
}
