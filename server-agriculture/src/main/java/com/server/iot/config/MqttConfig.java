//package com.server.iot.config;
//
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.integration.annotation.ServiceActivator;
//import org.springframework.integration.channel.DirectChannel;
//import org.springframework.integration.config.EnableIntegration;
//import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
//import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
//import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.MessageHandler;
//import org.springframework.beans.factory.InitializingBean;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Configuration
//@EnableIntegration
//public class MqttConfig implements InitializingBean {
//
//    @Value("${mqtt.username}")
//    private String username;
//
//    @Value("${mqtt.password}")
//    private String password;
//
//    @Value("${mqtt.server-uri}")
//    private String serverUri;
//
//    @Value("${mqtt.client-id}")
//    private String clientId;
//
//    @Value("${mqtt.default-topic}")
//    private String defaultTopic;
//
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        log.info("MQTT配置初始化完成 - serverUri: {}, clientId: {}, defaultTopic: {}",
//                serverUri, clientId, defaultTopic);
//
//        // 验证配置参数
//        if (serverUri == null || serverUri.trim().isEmpty()) {
//            throw new IllegalStateException("MQTT server-uri 配置不能为空");
//        }
//        if (clientId == null || clientId.trim().isEmpty()) {
//            throw new IllegalStateException("MQTT client-id 配置不能为空");
//        }
//        if (defaultTopic == null || defaultTopic.trim().isEmpty()) {
//            throw new IllegalStateException("MQTT default-topic 配置不能为空");
//        }
//    }
//
//    @Bean
//    public MqttPahoClientFactory mqttClientFactory() {
//        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
//        MqttConnectOptions options = new MqttConnectOptions();
//
//        log.info("配置MQTT连接: serverUri={}, username={}, clientId={}", serverUri, username, clientId);
//
//        options.setServerURIs(new String[]{serverUri});
//        options.setUserName(username);
//        options.setPassword(password.toCharArray());
//        options.setCleanSession(true);
//
//        // 添加连接超时和保持连接配置
//        options.setConnectionTimeout(30);
//        options.setKeepAliveInterval(60);
//        options.setAutomaticReconnect(true);
//
//        // 设置遗嘱消息（可选）
//        options.setWill("fish-dish/status", "offline".getBytes(), 1, false);
//
//        factory.setConnectionOptions(options);
//        return factory;
//    }
//
//    @Bean
//    public MessageChannel mqttOutboundChannel() {
//        log.info("创建MQTT出站通道: mqttOutboundChannel");
//        DirectChannel channel = new DirectChannel();
//        // 添加错误处理
//        channel.setFailover(false);
//        return channel;
//    }
//
//    @Bean
//    @ServiceActivator(inputChannel = "mqttOutboundChannel")
//    public MessageHandler mqttOutbound() {
//        try {
//            String outboundClientId = clientId + "_outbound_" + System.currentTimeMillis();
//            MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(
//                outboundClientId,
//                mqttClientFactory()
//            );
//            messageHandler.setAsync(true);
//            messageHandler.setDefaultTopic(defaultTopic);
//            messageHandler.setDefaultQos(1);
//
//            log.info("MQTT消息处理器配置完成: clientId={}, defaultTopic={}",
//                    outboundClientId, defaultTopic);
//
//            return messageHandler;
//        } catch (Exception e) {
//            log.error("MQTT消息处理器配置失败: {}", e.getMessage(), e);
//            throw e;
//        }
//    }
//}
