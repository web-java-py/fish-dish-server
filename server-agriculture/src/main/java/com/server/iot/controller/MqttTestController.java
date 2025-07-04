//package com.server.iot.controller;
//
//import com.server.iot.gateway.MqttGateway;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@Slf4j
//@RestController
//@RequestMapping("/mqtt/test")
//public class MqttTestController {
//
//    @Autowired
//    private MqttGateway mqttGateway;
//
//    @GetMapping("/send")
//    public String testMqttSend(@RequestParam(defaultValue = "Hello MQTT") String message,
//                               @RequestParam(defaultValue = "/fish-dish/test") String topic) {
//        try {
//            log.info("测试MQTT发送消息: topic={}, message={}", topic, message);
//            mqttGateway.sendToMqtt(message, topic);
//            return "MQTT消息发送成功: " + message;
//        } catch (Exception e) {
//            log.error("MQTT消息发送失败", e);
//            return "MQTT消息发送失败: " + e.getMessage();
//        }
//    }
//
//    @GetMapping("/status")
//    public String getMqttStatus() {
//        try {
//            // 发送一个测试消息来验证连接
//            mqttGateway.sendToMqtt("MQTT连接测试", "/fish-dish/status");
//            return "MQTT连接正常";
//        } catch (Exception e) {
//            log.error("MQTT连接测试失败", e);
//            return "MQTT连接失败: " + e.getMessage();
//        }
//    }
//}
