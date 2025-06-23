package com.server.iot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.domain.AgricultureWaterQualityData;
import com.server.domain.AgricultureWeatherData;
import com.server.iot.gateway.MqttGateway;
import com.server.service.AgricultureWaterQualityDataService;
import com.server.service.AgricultureWeatherDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class DataProcessingService {

    private static final Logger log = LoggerFactory.getLogger(DataProcessingService.class);

    @Autowired
    private AgricultureWaterQualityDataService waterQualityDataService;

    @Autowired
    private AgricultureWeatherDataService weatherDataService;

    @Autowired
    private MqttGateway mqttGateway;

    @Autowired
    private ObjectMapper objectMapper;


    public void processAndStore(byte[] data) {
        try {
            String dataString = new String(data);
            Map<String, Object> parsedData = parseData(dataString);
            String type = (String) parsedData.get("type");
            mqttGateway.sendToMqtt("测试测试MQTT", "/fish-dish/water");
            if ("water".equals(type)) {
                AgricultureWaterQualityData waterData = createWaterQualityData(parsedData);
                waterQualityDataService.save(waterData);
                log.info("成功保存水质数据: {}", waterData);
                // 发布到MQTT
                 mqttGateway.sendToMqtt(objectMapper.writeValueAsString(waterData), "/fish-dish/water");

            } else if ("weather".equals(type)) {
                AgricultureWeatherData weatherData = createWeatherData(parsedData);
                weatherDataService.save(weatherData);
                log.info("成功保存气象数据: {}", weatherData);
                // 发布到MQTT
                 mqttGateway.sendToMqtt(objectMapper.writeValueAsString(weatherData), "/fish-dish/weather");
            } else {
                log.warn("收到未知数据类型: {}", type);
            }

        } catch (Exception e) {
            log.error("处理并存储数据失败", e);
        }
    }

    /**
     * 处理Map类型的数据（从传感器通信服务调用）
     * @param parsedData 已解析的数据Map
     */
    public void processAndStore(Map<String, Object> parsedData) {
        try {
            String type = (String) parsedData.get("type");
            
            if ("water".equals(type)) {
                AgricultureWaterQualityData waterData = createWaterQualityData(parsedData);
                waterQualityDataService.save(waterData);
                log.info("成功保存水质数据: {}", waterData);
                // 发布到MQTT
                try {
                    mqttGateway.sendToMqtt(objectMapper.writeValueAsString(waterData), "/fish-dish/water");
                    log.info("水质数据成功发布到MQTT");
                } catch (Exception e) {
                    log.error("水质数据发布到MQTT失败: {}", e.getMessage(), e);
                }

            } else if ("weather".equals(type)) {
                AgricultureWeatherData weatherData = createWeatherData(parsedData);
                weatherDataService.save(weatherData);
                log.info("成功保存气象数据: {}", weatherData);
                // 发布到MQTT
                try {
                    log.info("气象数据发布到MQTT");
                    mqttGateway.sendToMqtt(objectMapper.writeValueAsString(weatherData), "/fish-dish/weather");
                    log.info("气象数据成功发布到MQTT");
                } catch (Exception e) {
                    log.error("气象数据发布到MQTT失败: {}", e.getMessage(), e);
                }
            } else {
                log.warn("收到未知数据类型: {}", type);
            }

        } catch (Exception e) {
            log.error("处理并存储已解析的数据失败", e);
        }
    }

    /**
     * 解析传入的字符串数据。
     * @param dataString 从串口读取的原始数据字符串
     * @return 一个包含解析后数据的Map
     */
    private Map<String, Object> parseData(String dataString) throws Exception {
        return objectMapper.readValue(dataString, HashMap.class);
    }

    private AgricultureWaterQualityData createWaterQualityData(Map<String, Object> data) {
        AgricultureWaterQualityData waterData = new AgricultureWaterQualityData();
        waterData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
        
        // 设置 大棚id 和 分区id
        String pastureId = (String) data.get("pastureId");
        String batchId = (String) data.get("batchId");
        waterData.setPastureId(pastureId);
        waterData.setBatchId(batchId);
        
        waterData.setPhValue(getDoubleValue(data.get("ph_value"), 0.0));
        waterData.setDissolvedOxygen(getDoubleValue(data.get("dissolved_oxygen"), 0.0));
        waterData.setAmmoniaNitrogen(getDoubleValue(data.get("ammonia_nitrogen"), 0.0));
        waterData.setWaterTemperature(getDoubleValue(data.get("water_temperature"), 0.0));
        waterData.setConductivity(getDoubleValue(data.get("conductivity"), 0.0));
        waterData.setCollectTime(LocalDateTime.now());
        return waterData;
    }

    private AgricultureWeatherData createWeatherData(Map<String, Object> data) {
        AgricultureWeatherData weatherData = new AgricultureWeatherData();
        weatherData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
        
        // 设置 pastureId 和 batchId
        String pastureId = (String) data.get("pastureId");
        String batchId = (String) data.get("batchId");
        weatherData.setPastureId(pastureId);
        weatherData.setBatchId(batchId);
        
        // 安全地设置数值字段，如果为null则使用默认值0.0
        weatherData.setTemperature(getDoubleValue(data.get("temperature"), 0.0));
        weatherData.setHumidity(getDoubleValue(data.get("humidity"), 0.0));
        weatherData.setWindSpeed(getDoubleValue(data.get("wind_speed"), 0.0));
        weatherData.setLightIntensity(getDoubleValue(data.get("light_intensity"), 0.0));
        weatherData.setRainfall(getDoubleValue(data.get("rainfall"), 0.0));
        weatherData.setAirPressure(getDoubleValue(data.get("air_pressure"), 0.0));
        
        // 风向是字符串类型，直接设置
        String windDirection = (String) data.get("wind_direction");
        weatherData.setWindDirection(windDirection != null ? windDirection : "");
        
        weatherData.setCollectTime(LocalDateTime.now());
        return weatherData;
    }
    
    /**
     * 安全地获取Double值，如果为null或无法转换则返回默认值
     */
    private Double getDoubleValue(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            } else {
                String strValue = Objects.toString(value);
                if ("null".equals(strValue) || strValue.trim().isEmpty()) {
                    return defaultValue;
                }
                return new java.math.BigDecimal(strValue).doubleValue();
            }
        } catch (Exception e) {
            log.warn("无法转换值 {} 为Double类型，使用默认值 {}", value, defaultValue);
            return defaultValue;
        }
    }
} 