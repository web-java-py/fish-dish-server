//package com.server.iot.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.server.domain.AgricultureDeviceMqttConfig;
//import com.server.domain.AgricultureWaterQualityData;
//import com.server.domain.AgricultureWeatherData;
//import com.server.iot.gateway.MqttGateway;
//import com.server.service.AgricultureDeviceMqttConfigService;
//import com.server.service.AgricultureWaterQualityDataService;
//import com.server.service.AgricultureWeatherDataService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Objects;
//
///**
// * 数据处理服务
// * 负责对从传感器接收并解析后的数据进行后续处理。
// */
//@Service
//public class DataProcessingService {
//
//    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);
//
//    @Autowired
//    private AgricultureWaterQualityDataService waterQualityDataService; // 水质数据
//
//    @Autowired
//    private AgricultureWeatherDataService weatherDataService; // 气象数据
//
//    @Autowired
//    private AgricultureDeviceMqttConfigService deviceMqttConfigService; //Mqtt配置
//    @Autowired
//    private MqttGateway mqttGateway; // MQTT消息推送网关
//
//    @Autowired
//    private ObjectMapper objectMapper; // Spring Boot自动配置的JSON处理工具
//
//
//    /**
//     * 处理字节数组形式的数据。
//     * 假设字节数组内容为JSON字符串。
//     * @param data 原始字节数组数据
//     */
//    public void processAndStore(byte[] data) {
//        try {
//            String dataString = new String(data);
//            Map<String, Object> parsedData = parseData(dataString);
//            String type = (String) parsedData.get("type");
//            if ("water".equals(type)) {
//                AgricultureWaterQualityData waterData = createWaterQualityData(parsedData);
//                waterQualityDataService.save(waterData); // 保存到数据库
//                log.info("成功保存水质数据: {}", waterData);
//                // 将数据对象转换为JSON字符串并发布到MQTT
//                 mqttGateway.sendToMqtt(objectMapper.writeValueAsString(waterData), "/fish-dish/water");
//
//            } else if ("weather".equals(type)) {
//                AgricultureWeatherData weatherData = createWeatherData(parsedData);
//                weatherDataService.save(weatherData); // 保存到数据库
//                log.info("成功保存气象数据: {}", weatherData);
//                // 将数据对象转换为JSON字符串并发布到MQTT
//                 mqttGateway.sendToMqtt(objectMapper.writeValueAsString(weatherData), "/fish-dish/weather");
//            } else {
//                log.warn("收到未知数据类型: {}", type);
//            }
//
//        } catch (Exception e) {
//            log.error("处理并存储数据失败", e);
//        }
//    }
//
//    /**
//     * 处理Map类型的数据，这是由SensorCommunicationService调用的主要方法。
//     * @param parsedData 从传感器通信服务传递过来的，已解析的数据Map
//     */
//    public void processAndStore(Map<String, Object> parsedData) {
//        try {
//            // 1. 获取设备ID
//            Long deviceId = null;
//            if (parsedData.get("deviceId") != null) {
//                deviceId = Long.valueOf(parsedData.get("deviceId").toString());
//            }
//
//            // 2. 查找该设备的MQTT配置，获取专属topic
//            String topic = null;
//            if (deviceId != null) {
//                AgricultureDeviceMqttConfig config = deviceMqttConfigService.getByDeviceId(deviceId);
//                if (config != null && config.getMqttTopic() != null && !config.getMqttTopic().isEmpty()) {
//                    topic = config.getMqttTopic();
//                }
//            }
//            // 兜底：如果没有查到topic，推送到一个默认主题
//            if (topic == null) {
//                topic = "/fish-dish/unknown";
//                log.warn("设备ID {} 未配置MQTT主题，推送到默认主题 {}", deviceId, topic);
//            }
//
//            // 3. 数据类型（决定存储到哪张表，但推送MQTT只看设备ID）
//            String type = (String) parsedData.get("type");
//
//            if ("water".equals(type)) {
//                // 水质数据，存表
//                AgricultureWaterQualityData waterData = createWaterQualityData(parsedData);
//                waterQualityDataService.save(waterData);
//                log.info("成功保存水质数据: {}", waterData);
//                // 推送到设备专属topic
//                mqttGateway.sendToMqtt(objectMapper.writeValueAsString(waterData), topic);
//                log.info("水质数据成功发布到MQTT主题 {}", topic);
//            } else if ("weather".equals(type)) {
//                // 气象数据，存表
//                AgricultureWeatherData weatherData = createWeatherData(parsedData);
//                weatherDataService.save(weatherData);
//                log.info("成功保存气象数据: {}", weatherData);
//                // 推送到设备专属topic
//                mqttGateway.sendToMqtt(objectMapper.writeValueAsString(weatherData), topic);
//                log.info("气象数据成功发布到MQTT主题 {}", topic);
//            } else {
//                // 其它类型，直接推送原始数据
//                mqttGateway.sendToMqtt(objectMapper.writeValueAsString(parsedData), topic);
//                log.warn("收到未知数据类型: {}，原始数据已推送到MQTT主题 {}", type, topic);
//            }
//        } catch (Exception e) {
//            log.error("处理并存储已解析的数据失败", e);
//        }
//    }
//
//    /**
//     * （私有辅助方法）解析传入的JSON字符串数据。
//     * @param dataString 从外部接收的原始数据JSON字符串
//     * @return 一个包含解析后数据的Map
//     */
//    private Map<String, Object> parseData(String dataString) throws Exception {
//        return objectMapper.readValue(dataString, HashMap.class);
//    }
//
//    /**
//     * 根据传入的Map数据，创建一个水质数据实体对象。
//     * @param data 包含传感器数据的Map
//     * @return 构造好的AgricultureWaterQualityData实体
//     */
//    private AgricultureWaterQualityData createWaterQualityData(Map<String, Object> data) {
//        AgricultureWaterQualityData waterData = new AgricultureWaterQualityData();
//        waterData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
//
//        // 设置大棚ID和分区ID
//        waterData.setPastureId((String) data.get("pastureId"));
//        waterData.setBatchId((String) data.get("batchId"));
//
//        // 使用安全的getDoubleValue方法，避免因数据为null或格式错误导致程序崩溃
//        waterData.setPhValue(getDoubleValue(data.get("ph_value"), 0.0));
//        waterData.setDissolvedOxygen(getDoubleValue(data.get("dissolved_oxygen"), 0.0));
//        waterData.setAmmoniaNitrogen(getDoubleValue(data.get("ammonia_nitrogen"), 0.0));
//        waterData.setWaterTemperature(getDoubleValue(data.get("water_temperature"), 0.0));
//        waterData.setConductivity(getDoubleValue(data.get("conductivity"), 0.0));
//        waterData.setCollectTime(LocalDateTime.now()); // 设置数据采集时间为当前服务器时间
//        return waterData;
//    }
//
//    /**
//     * 根据传入的Map数据，创建一个气象数据实体对象。
//     * @param data 包含传感器数据的Map
//     * @return 构造好的AgricultureWeatherData实体
//     */
//    private AgricultureWeatherData createWeatherData(Map<String, Object> data) {
//        AgricultureWeatherData weatherData = new AgricultureWeatherData();
//        weatherData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
//
//        // 设置大棚ID和分区ID
//        weatherData.setPastureId((String) data.get("pastureId"));
//        weatherData.setBatchId((String) data.get("batchId"));
//
//        // 安全地设置数值字段，如果传入数据为null则使用默认值0.0
//        weatherData.setTemperature(getDoubleValue(data.get("temperature"), 0.0));
//        weatherData.setHumidity(getDoubleValue(data.get("humidity"), 0.0));
//        weatherData.setWindSpeed(getDoubleValue(data.get("wind_speed"), 0.0));
//        weatherData.setLightIntensity(getDoubleValue(data.get("light_intensity"), 0.0));
//        weatherData.setRainfall(getDoubleValue(data.get("rainfall"), 0.0));
//        weatherData.setAirPressure(getDoubleValue(data.get("air_pressure"), 0.0));
//
//        // 风向是字符串类型，进行null检查
//        String windDirection = (String) data.get("wind_direction");
//        weatherData.setWindDirection(windDirection != null ? windDirection : ""); // 如果为null，则设置为空字符串
//        return weatherData;
//    }
//
//    /**
//     * 安全地从一个Object对象获取Double值。
//     * @param value 待转换的对象
//     * @param defaultValue 如果转换失败或对象为null时返回的默认值
//     * @return 转换后的Double值或默认值
//     */
//    private Double getDoubleValue(Object value, Double defaultValue) {
//        if (value == null) {
//            return defaultValue;
//        }
//        try {
//            if (value instanceof Number) {
//                // 如果对象本身就是Number的子类（如Integer, Double），直接转换
//                return ((Number) value).doubleValue();
//            } else {
//                // 否则，将其转换为字符串再进行处理
//                String strValue = Objects.toString(value);
//                // 兼容"null"字符串或空字符串的情况
//                if ("null".equals(strValue) || strValue.trim().isEmpty()) {
//                    return defaultValue;
//                }
//                // 使用BigDecimal进行转换，可以避免精度问题，然后再转为double
//                return new java.math.BigDecimal(strValue).doubleValue();
//            }
//        } catch (Exception e) {
//            log.warn("无法转换值 '{}' 为Double类型，使用默认值 '{}'", value, defaultValue);
//            return defaultValue;
//        }
//    }
//}
