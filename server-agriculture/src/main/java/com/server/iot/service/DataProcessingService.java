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
        // TODO: 1. 解析数据
        // 你需要根据你的设备的数据协议来解析这段byte[]数据。
        // 这里我假设一个简单的JSON格式，例如:
        // { "type": "water", "deviceId": 1, "ph": 7.5, ... } or
        // { "type": "weather", "deviceId": 2, "temperature": 25.5, ... }
        // 你需要替换成你真正的解析逻辑。
        try {
            String dataString = new String(data);
            Map<String, Object> parsedData = parseData(dataString);
            String type = (String) parsedData.get("type");

            if ("water".equals(type)) {
                AgricultureWaterQualityData waterData = createWaterQualityData(parsedData);
                waterQualityDataService.save(waterData);
                log.info("Saved water quality data: {}", waterData);
                // 发布到MQTT
                 mqttGateway.sendToMqtt(objectMapper.writeValueAsString(waterData), "/fish-dish/water");

            } else if ("weather".equals(type)) {
                AgricultureWeatherData weatherData = createWeatherData(parsedData);
                weatherDataService.save(weatherData);
                log.info("Saved weather data: {}", weatherData);
                // 发布到MQTT
                 mqttGateway.sendToMqtt(objectMapper.writeValueAsString(weatherData), "/fish-dish/weather");
            } else {
                log.warn("Unknown data type received: {}", type);
            }

        } catch (Exception e) {
            log.error("Failed to process and store data", e);
        }
    }

    /**
     * 解析传入的字符串数据。
     * !!! 这个方法是一个示例，你需要根据你的实际数据格式来实现它 !!!
     * @param dataString 从串口读取的原始数据字符串
     * @return 一个包含解析后数据的Map
     */
    private Map<String, Object> parseData(String dataString) throws Exception {
        // 假设是JSON格式
        return objectMapper.readValue(dataString, HashMap.class);
    }

    private AgricultureWaterQualityData createWaterQualityData(Map<String, Object> data) {
        AgricultureWaterQualityData waterData = new AgricultureWaterQualityData();
        // 你需要从map中提取数据并设置到对象中
        // 注意：这里的key需要和你解析出的map的key一致
        waterData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
        waterData.setPhValue(new java.math.BigDecimal(Objects.toString(data.get("ph_value"))).doubleValue());
        waterData.setDissolvedOxygen(new java.math.BigDecimal(Objects.toString(data.get("dissolved_oxygen"))).doubleValue());
        waterData.setAmmoniaNitrogen(new java.math.BigDecimal(Objects.toString(data.get("ammonia_nitrogen"))).doubleValue());
        waterData.setWaterTemperature(new java.math.BigDecimal(Objects.toString(data.get("water_temperature"))).doubleValue());
        waterData.setConductivity(new java.math.BigDecimal(Objects.toString(data.get("conductivity"))).doubleValue());
        waterData.setCollectTime(LocalDateTime.now());
        return waterData;
    }

    private AgricultureWeatherData createWeatherData(Map<String, Object> data) {
        AgricultureWeatherData weatherData = new AgricultureWeatherData();
        weatherData.setDeviceId(Long.valueOf(Objects.toString(data.get("deviceId"))));
        weatherData.setTemperature(new java.math.BigDecimal(Objects.toString(data.get("temperature"))).doubleValue());
        weatherData.setHumidity(new java.math.BigDecimal(Objects.toString(data.get("humidity"))).doubleValue());
        weatherData.setWindSpeed(new java.math.BigDecimal(Objects.toString(data.get("wind_speed"))).doubleValue());
        weatherData.setWindDirection((String) data.get("wind_direction"));
        weatherData.setLightIntensity(new java.math.BigDecimal(Objects.toString(data.get("light_intensity"))).doubleValue());
        weatherData.setRainfall(new java.math.BigDecimal(Objects.toString(data.get("rainfall"))).doubleValue());
        weatherData.setAirPressure(new java.math.BigDecimal(Objects.toString(data.get("air_pressure"))).doubleValue());
        weatherData.setCollectTime(LocalDateTime.now());
        return weatherData;
    }
} 