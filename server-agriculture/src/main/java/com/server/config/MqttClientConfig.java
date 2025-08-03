package com.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT光照客户端配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "mqttclient")
public class MqttClientConfig {
    
    /**
     * MQTT服务器地址
     */
    private String mqttbroker;
    
    /**
     * 光照主题
     */
    private String lighttopic;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
} 