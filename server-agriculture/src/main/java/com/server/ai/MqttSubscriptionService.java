package com.server.ai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.domain.dto.MqttDeviceDataDTO;
import com.server.service.AgricultureDeviceMqttConfigService;
import com.server.service.AgricultureDeviceService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
@Service
public class MqttSubscriptionService {

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;

    @Autowired
    private AgricultureDeviceMqttConfigService deviceMqttConfigService;

    @Autowired
    private AiOnlineDeviceService aiOnlineDeviceService;

    private final Map<String, MqttClient> mqttClients = new ConcurrentHashMap<>();
    private final Map<String, String> topicToDeviceMap = new ConcurrentHashMap<>();
    private final Map<String, Long> topicToDeviceIdMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("初始化MQTT订阅服务...");
        subscribeAllDevices();
    }

    @PreDestroy
    public void destroy() {
        log.info("关闭MQTT订阅服务...");
        mqttClients.values().forEach(client -> {
            try {
                if (client.isConnected()) {
                    client.disconnect();
                }
                client.close();
            } catch (MqttException e) {
                log.error("关闭MQTT客户端失败", e);
            }
        });
        mqttClients.clear();
    }

    /**
     * 订阅所有设备的MQTT主题
     */
    public void subscribeAllDevices() {
        try {
            // 获取所有设备
            List<AgricultureDevice> devices = agricultureDeviceService.list();
            
            for (AgricultureDevice device : devices) {
                // 获取设备的MQTT配置
                AgricultureDeviceMqttConfig mqttConfig = deviceMqttConfigService.getByDeviceId(device.getId());
                
                if (mqttConfig != null && mqttConfig.getMqttTopic() != null) {
                    subscribeDevice(device, mqttConfig);
                } else {
                    log.warn("设备 {} 没有MQTT配置", device.getDeviceName());
                }
            }
            
            log.info("成功订阅了 {} 个设备的MQTT主题", mqttClients.size());
        } catch (Exception e) {
            log.error("订阅设备MQTT主题失败", e);
        }
    }

    /**
     * 订阅单个设备的MQTT主题
     */
    private void subscribeDevice(AgricultureDevice device, AgricultureDeviceMqttConfig mqttConfig) {
        try {
            String brokerKey = mqttConfig.getMqttBroker();
            String topic = mqttConfig.getMqttTopic();
            
            // 检查是否已经为这个broker创建了客户端
            MqttClient client = mqttClients.get(brokerKey);
            
            if (client == null) {
                // 创建新的MQTT客户端
                String clientId = "ai_device_subscriber_" + System.currentTimeMillis();
                client = new MqttClient(brokerKey, clientId, new MemoryPersistence());
                
                // 配置连接选项
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                options.setConnectionTimeout(30);
                options.setKeepAliveInterval(60);
                options.setAutomaticReconnect(true);
                
                // 设置用户名和密码（如果有）
                if (mqttConfig.getMqttUsername() != null && !mqttConfig.getMqttUsername().isEmpty()) {
                    options.setUserName(mqttConfig.getMqttUsername());
                    options.setPassword(mqttConfig.getMqttPassword().toCharArray());
                }
                
                // 连接MQTT服务器
                client.connect(options);
                
                // 设置回调
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        log.error("MQTT连接丢失: {}", cause.getMessage());
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) {
                        handleMqttMessage(topic, message);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        // 发布完成回调，这里不需要处理
                    }
                });
                
                mqttClients.put(brokerKey, client);
                log.info("成功连接到MQTT服务器: {}", brokerKey);
            }
            
            // 订阅主题
            client.subscribe(topic, mqttConfig.getMqttQos().intValue());
            topicToDeviceMap.put(topic, device.getDeviceName());
            topicToDeviceIdMap.put(topic, device.getId());
            
            log.info("成功订阅设备 {} 的主题: {}", device.getDeviceName(), topic);
            
        } catch (MqttException e) {
            log.error("订阅设备 {} 的MQTT主题失败: {}", device.getDeviceName(), e.getMessage(), e);
        }
    }

    /**
     * 处理接收到的MQTT消息
     */
    private void handleMqttMessage(String topic, MqttMessage message) {
        try {
            String payload = new String(message.getPayload());
            String deviceName = topicToDeviceMap.get(topic);
            Long deviceId = topicToDeviceIdMap.get(topic);
            
            log.info("收到MQTT消息 - 主题: {}, 设备: {}, 数据: {}", 
                    topic, deviceName, payload);
            
            // 解析JSON数据
            JSONObject jsonData = JSON.parseObject(payload);
            JSONObject data = jsonData.getJSONObject("data");
            
            if (data != null) {
                // 转换为DTO
                MqttDeviceDataDTO deviceData = convertToDeviceData(data);
                
                // 处理设备数据并传递给AI
                processDeviceDataForAI(deviceData);
            }
            
        } catch (Exception e) {
            log.error("处理MQTT消息失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 处理设备数据并传递给AI
     */
    private void processDeviceDataForAI(MqttDeviceDataDTO deviceData) {
        try {
            // 构建包含设备数据的prompt
            String deviceDataPrompt = buildDeviceDataPrompt(deviceData);
            
            log.info("收到设备数据，准备传递给AI处理: {}", deviceDataPrompt);
            
            // 调用online方法处理设备数据
            aiOnlineDeviceService.online(deviceDataPrompt);
            
        } catch (Exception e) {
            log.error("处理设备数据传递给AI失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 构建包含设备数据的prompt
     */
    private String buildDeviceDataPrompt(MqttDeviceDataDTO deviceData) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("收到新的设备数据，请分析并给出建议：\n\n");
        
        // 基本信息
        prompt.append("设备ID: ").append(deviceData.getDeviceId()).append("\n");
        prompt.append("大棚ID: ").append(deviceData.getPastureId()).append("\n");
        prompt.append("分区ID: ").append(deviceData.getBatchId()).append("\n");
        prompt.append("采集时间: ").append(deviceData.getCollectTime()).append("\n\n");
        
        // 气象数据
        if (deviceData.getTemperature() != null || deviceData.getHumidity() != null) {
            prompt.append("气象数据:\n");
            if (deviceData.getTemperature() != null) {
                prompt.append("- 温度: ").append(deviceData.getTemperature()).append("°C\n");
            }
            if (deviceData.getHumidity() != null) {
                prompt.append("- 湿度: ").append(deviceData.getHumidity()).append("%\n");
            }
            if (deviceData.getWindSpeed() != null) {
                prompt.append("- 风速: ").append(deviceData.getWindSpeed()).append("m/s\n");
            }
            if (deviceData.getWindDirection() != null && !deviceData.getWindDirection().isEmpty()) {
                prompt.append("- 风向: ").append(deviceData.getWindDirection()).append("\n");
            }
            if (deviceData.getLightIntensity() != null) {
                prompt.append("- 光照强度: ").append(deviceData.getLightIntensity()).append("lux\n");
            }
            if (deviceData.getRainfall() != null) {
                prompt.append("- 降雨量: ").append(deviceData.getRainfall()).append("mm\n");
            }
            if (deviceData.getAirPressure() != null) {
                prompt.append("- 气压: ").append(deviceData.getAirPressure()).append("hPa\n");
            }
            prompt.append("\n");
        }
        
        // 水质数据
        if (deviceData.getPhValue() != null || deviceData.getDissolvedOxygen() != null) {
            prompt.append("水质数据:\n");
            if (deviceData.getWaterTemperature() != null) {
                prompt.append("- 水温: ").append(deviceData.getWaterTemperature()).append("°C\n");
            }
            if (deviceData.getPhValue() != null) {
                prompt.append("- pH值: ").append(deviceData.getPhValue()).append("\n");
            }
            if (deviceData.getDissolvedOxygen() != null) {
                prompt.append("- 溶解氧: ").append(deviceData.getDissolvedOxygen()).append("mg/L\n");
            }
            if (deviceData.getConductivity() != null) {
                prompt.append("- 电导率: ").append(deviceData.getConductivity()).append("μS/cm\n");
            }
            if (deviceData.getAmmoniaNitrogen() != null) {
                prompt.append("- 氨氮: ").append(deviceData.getAmmoniaNitrogen()).append("mg/L\n");
            }
            prompt.append("\n");
        }
        
        prompt.append("请根据以上数据进行分析，并提供相应的建议和措施。");
        
        return prompt.toString();
    }

    /**
     * 将JSON数据转换为DTO
     */
    private MqttDeviceDataDTO convertToDeviceData(JSONObject data) {
        MqttDeviceDataDTO deviceData = new MqttDeviceDataDTO();
        
        deviceData.setId(data.getLong("id"));
        deviceData.setDeviceId(data.getLong("deviceId"));
        deviceData.setPastureId(data.getString("pastureId"));
        deviceData.setBatchId(data.getString("batchId"));
        deviceData.setRemark(data.getString("remark"));
        
        // 解析时间
        String collectTimeStr = data.getString("collectTime");
        if (collectTimeStr != null) {
            try {
                // 处理ISO 8601格式的时间
                LocalDateTime collectTime = LocalDateTime.parse(collectTimeStr, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"));
                deviceData.setCollectTime(collectTime);
            } catch (Exception e) {
                log.warn("解析时间失败: {}", collectTimeStr);
            }
        }
        
        // 气象数据字段
        deviceData.setWindSpeed(data.getDouble("windSpeed"));
        deviceData.setWindDirection(data.getString("windDirection"));
        deviceData.setTemperature(data.getDouble("temperature"));
        deviceData.setHumidity(data.getDouble("humidity"));
        deviceData.setLightIntensity(data.getDouble("lightIntensity"));
        deviceData.setRainfall(data.getDouble("rainfall"));
        deviceData.setAirPressure(data.getDouble("airPressure"));
        
        // 水质数据字段
        deviceData.setWaterTemperature(data.getDouble("waterTemperature"));
        deviceData.setPhValue(data.getDouble("phValue"));
        deviceData.setDissolvedOxygen(data.getDouble("dissolvedOxygen"));
        deviceData.setConductivity(data.getDouble("conductivity"));
        deviceData.setAmmoniaNitrogen(data.getDouble("ammoniaNitrogen"));
        
        return deviceData;
    }

    /**
     * 重新订阅所有设备
     */
    public void resubscribeAllDevices() {
        log.info("重新订阅所有设备的MQTT主题...");
        
        // 断开所有现有连接
        mqttClients.values().forEach(client -> {
            try {
                if (client.isConnected()) {
                    client.disconnect();
                }
            } catch (MqttException e) {
                log.error("断开MQTT连接失败", e);
            }
        });
        
        mqttClients.clear();
        topicToDeviceMap.clear();
        topicToDeviceIdMap.clear();
        
        // 重新订阅
        subscribeAllDevices();
    }
} 