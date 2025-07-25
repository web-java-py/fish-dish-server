package com.server.ai.tool;

import lombok.Data;

//请求体
@Data
public class DeviceControlRequest {
    private Long deviceId;
    private String action; // "on" or "off"
    private Integer index;
} 