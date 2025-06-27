package com.server.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ WebSocket服务
 */
@Slf4j
@Component
@ServerEndpoint("/ws/mq/{id}")
public class MqSocketServer {

    private static Map<String, Session> sessionMap = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("RabbitMQ客户端：" + id + "建立连接");
        sessionMap.put(id, session);
    }

    @OnMessage
    public void onMessage(String message, @PathParam("id") String id) {
        log.info("收到来自RabbitMQ客户端：" + id + "的信息:" + message);
    }

    @OnClose
    public void onClose(@PathParam("id") String id) {
        log.info("RabbitMQ客户端连接断开:" + id);
        sessionMap.remove(id);
    }

    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnError
    public void onError(Throwable t) {
        log.error("RabbitMQ客户端异常");
        t.printStackTrace();
    }
}
