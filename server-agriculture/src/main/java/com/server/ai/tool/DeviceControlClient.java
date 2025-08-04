package com.server.ai.tool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//设备控制
@Component
public class DeviceControlClient {
    private static final Logger logger = LoggerFactory.getLogger(DeviceControlClient.class);

    //设备控制api地址
    private static final String URL = "http://192.168.31.120:8081/deviceOperation/control";

    @Autowired
    private RestTemplate restTemplate;

    public String controlDevice(DeviceControlRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<DeviceControlRequest> entity = new HttpEntity<>(request, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(URL, entity, String.class);
            return response.getBody();
        } catch (Exception e) {
            logger.error("设备控制请求失败", e);
            return "设备控制失败: " + e.getMessage();
        }
    }
}
