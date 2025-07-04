//package com.server.iot.gateway;
//
//import org.springframework.integration.annotation.MessagingGateway;
//import org.springframework.integration.mqtt.support.MqttHeaders;
//import org.springframework.messaging.handler.annotation.Header;
//import org.springframework.stereotype.Component;
//
//@Component
//@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
//public interface MqttGateway {
//
//    void sendToMqtt(String payload);
//
//    void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic);
//
//    void sendToMqtt(String payload, @Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos);
//}
