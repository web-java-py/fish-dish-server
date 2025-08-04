package com.server.ai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.server.config.MqttClientConfig;
import com.server.domain.dto.MqttDeviceDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@ConditionalOnProperty(name = "online.enabled", havingValue = "true")
public class MqttSubscriptionService {

    @Autowired
    private MqttClientConfig mqttClientConfig;

    @Autowired
    private AiService aiService;

    private MqttClient mqttClient;

    @PostConstruct
    public void init() {
        log.info("初始化MQTT订阅服务...");

        // 验证配置
        log.info("MQTT配置 - Broker: {}, Topic: {}, Username: {}",
                mqttClientConfig.getMqttbroker(),
                mqttClientConfig.getLighttopic(),
                mqttClientConfig.getUsername());

        subscribeLightTopic();
    }

    @PreDestroy
    public void destroy() {
        log.info("关闭MQTT订阅服务...");
        if (mqttClient != null) {
            try {
                if (mqttClient.isConnected()) {
                    mqttClient.disconnect();
                }
                mqttClient.close();
            } catch (MqttException e) {
                log.error("关闭MQTT客户端失败", e);
            }
        }
    }

    /**
     * 订阅光照主题
     */
    private void subscribeLightTopic() {
        try {
            // 从配置中获取MQTT服务器地址
            String broker = mqttClientConfig.getMqttbroker();
            String lightTopic = mqttClientConfig.getLighttopic();
            String username = mqttClientConfig.getUsername();
            String password = mqttClientConfig.getPassword();

            // 创建客户端ID
            String clientId = "ai_light_subscriber_" + System.currentTimeMillis();

            // 创建MQTT客户端
            mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());

            // 配置连接选项
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setConnectionTimeout(30);
            options.setKeepAliveInterval(60);
            options.setAutomaticReconnect(true);

            // 设置用户名和密码
            if (username != null && !username.isEmpty()) {
                options.setUserName(username);
                options.setPassword(password.toCharArray());
            }

            // 连接MQTT服务器
            mqttClient.connect(options);

            // 设置回调
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    log.error("MQTT连接丢失: {}", cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    handleLightMessage(topic, message);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // 发布完成回调，这里不需要处理
                }
            });

            // 订阅光照主题
            mqttClient.subscribe(lightTopic, 1);

            log.info("成功连接到MQTT服务器: {} 并订阅光照主题: {}", broker, lightTopic);

        } catch (MqttException e) {
            log.error("订阅光照主题失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理接收到的光照MQTT消息
     */
    private void handleLightMessage(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());

            // 解析JSON数据
            JSONObject lightData = JSON.parseObject(payload);

            // 转换为DTO
            MqttDeviceDataDTO deviceData = convertToLightData(lightData);

            // 处理光照数据并传递给AI
            processLightDataForAI(deviceData);

        } catch (Exception e) {
            log.error("处理光照MQTT消息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理光照数据并传递给AI
     */
    private void processLightDataForAI(MqttDeviceDataDTO deviceData) {
        try {
            // 构建包含光照数据的prompt
            String lightDataPrompt = buildLightDataPrompt(deviceData);

            // 调用online方法处理光照数据
            aiService.onlineAgents(lightDataPrompt);

        } catch (Exception e) {
            log.error("处理光照数据传递给AI失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 构建包含光照数据的prompt
     */
    private String buildLightDataPrompt(MqttDeviceDataDTO deviceData) {
        StringBuilder prompt = new StringBuilder();

        // 基本信息
        prompt.append("设备ID: ").append(deviceData.getDeviceId()).append("\n");

        // 光照数据
        if (deviceData.getLightIntensity() != null) {
            prompt.append("光照数据:\n");
            prompt.append("- 光照强度(light_intensity): ").append(deviceData.getLightIntensity()).append(" lux\n");
        }

        // 增强提示词
        prompt.append("当光照数据在阈值范围内，保持当前设备运行。当光照数据大于阈值范围，启动推杆（打开遮阳棚降低光照强度），当光照数据小于阈值范围，关闭推杆（关闭遮阳棚增强光照强度）。");
        return prompt.toString();
    }

    /**
     * 将JSON数据转换为光照DTO
     */
    private MqttDeviceDataDTO convertToLightData(JSONObject data) {
        MqttDeviceDataDTO deviceData = new MqttDeviceDataDTO();

        deviceData.setId(data.getLong("id"));
        deviceData.setDeviceId(data.getLong("deviceId"));
        deviceData.setPastureId(data.getString("pastureId"));
        deviceData.setBatchId(data.getString("batchId"));
        deviceData.setRemark(data.getString("remark"));

        // 解析时间
        String collectTimeStr = data.getString("collectTime");
        if (collectTimeStr != null && !collectTimeStr.isEmpty()) {
            try {
                LocalDateTime collectTime = null;

                // 尝试多种时间格式
                try {
                    // 格式1: yyyy-MM-dd'T'HH:mm:ss.SSSSSS (6位微秒)
                    collectTime = LocalDateTime.parse(collectTimeStr,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
                } catch (Exception e1) {
                    try {
                        // 格式2: yyyy-MM-dd'T'HH:mm:ss.SSSSSSS (7位微秒)
                        collectTime = LocalDateTime.parse(collectTimeStr,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"));
                    } catch (Exception e2) {
                        try {
                            // 格式3: yyyy-MM-dd'T'HH:mm:ss.SSS (3位毫秒)
                            collectTime = LocalDateTime.parse(collectTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
                        } catch (Exception e3) {
                            // 格式4: yyyy-MM-dd'T'HH:mm:ss (无毫秒)
                            collectTime = LocalDateTime.parse(collectTimeStr,
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
                        }
                    }
                }

                if (collectTime != null) {
                    deviceData.setCollectTime(collectTime);
                }
            } catch (Exception e) {
                log.warn("解析时间失败: {}, 错误: {}", collectTimeStr, e.getMessage());
            }
        }

        // 只处理光照数据字段
        deviceData.setLightIntensity(data.getDouble("lightIntensity"));

        return deviceData;
    }

    /**
     * 重新订阅光照主题
     */
    public void resubscribeLightTopic() {
        log.info("重新订阅光照主题...");

        // 断开现有连接
        if (mqttClient != null) {
            try {
                if (mqttClient.isConnected()) {
                    mqttClient.disconnect();
                }
            } catch (MqttException e) {
                log.error("断开MQTT连接失败", e);
            }
        }

        // 重新订阅
        subscribeLightTopic();
    }
}
