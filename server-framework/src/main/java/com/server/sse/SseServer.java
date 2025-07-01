package com.server.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SseServer {

    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 创建连接
     *
     * @param uid
     * @return
     */
    public SseEmitter createSse(String uid) {
        //默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        //完成后回调
        sseEmitter.onCompletion(() -> {
            sseEmitterMap.remove(uid);
            log.info("[{}]结束连接...................", uid);
        });
        //超时回调
        sseEmitter.onTimeout(() -> {
            log.info("[{}]连接超时...................", uid);
        });
        //异常回调
        sseEmitter.onError(
                throwable -> {
                    try {
                        log.info("[{}]连接异常,{}", uid, throwable.toString());
                        sseEmitter.send(SseEmitter.event()
                                .id(uid)
                                .name("发生异常！")
                                .data("发生异常请重试！")
                                .reconnectTime(3000));
                        sseEmitterMap.put(uid, sseEmitter);
                    } catch (IOException e) {
                        log.info(e.getMessage());
                    }
                }
        );
        try {
            sseEmitter.send(SseEmitter.event().reconnectTime(5000));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        sseEmitterMap.put(uid, sseEmitter);
        log.info("[{}]创建sse连接成功！", uid);
        return sseEmitter;
    }

    /**
     * 发送消息
     *
     * @param messageId
     * @param message
     */
    public void sendToAllMessage(String messageId, Object message) {
        Collection<SseEmitter> sseEmitters = sseEmitterMap.values();
        for (SseEmitter sseEmitter : sseEmitters) {
            try {
                sseEmitter.send(SseEmitter.event().id(messageId).reconnectTime(1 * 60 * 1000L).data(message));
                log.info("消息id:{} 推送成功:{}", messageId, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭连接
     *
     * @param uid
     */
    public void closeSse(String uid) {
        if (sseEmitterMap.containsKey(uid)) {
            sseEmitterMap.get(uid).complete();
            sseEmitterMap.remove(uid);
            log.info("[{}]sse关闭成功！", uid);
        }
    }
}
