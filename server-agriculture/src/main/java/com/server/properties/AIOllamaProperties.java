package com.server.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ai.ollama")
@Data
public class AIOllamaProperties {
    private String baseUrl;
    private String modelName;
    private Double temperature;
    private Integer timeout;
}
