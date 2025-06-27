package com.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "ai.local")
public class AiLocalProperties {
    private static String vlUrl;
    private static Integer timeout;

    public static String getVlUrl() {
        return vlUrl;
    }

    public void setVlUrl(String vlUrl) {
        AiLocalProperties.vlUrl = vlUrl;
    }

    public static Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        AiLocalProperties.timeout = timeout;
    }
}
