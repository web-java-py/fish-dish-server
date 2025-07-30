package com.server.domain.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * MQTT设备数据DTO
 */
@Data
public class MqttDeviceDataDTO {
    
    private Long id;
    private Long deviceId;
    private String pastureId;
    private String batchId;
    private LocalDateTime collectTime;
    private String remark;
    
    // 气象数据字段
    private Double windSpeed;
    private String windDirection;
    private Double temperature;
    private Double humidity;
    private Double lightIntensity;
    private Double rainfall;
    private Double airPressure;
    
    // 水质数据字段
    private Double waterTemperature;
    private Double phValue;
    private Double dissolvedOxygen;
    private Double conductivity;
    private Double ammoniaNitrogen;
} 