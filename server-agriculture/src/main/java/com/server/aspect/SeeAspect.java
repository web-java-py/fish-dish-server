package com.server.aspect;

import com.server.annotation.SeeConsole;
import com.server.constant.SseConstant;
import com.server.service.AgricultureConsoleService;
import com.server.sse.SseServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Slf4j
public class SeeAspect {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    @After("@annotation(seeConsole)")
    public void after(SeeConsole seeConsole) {
        Map<String, Object> map = agricultureConsoleService.listTotal();
        String console = SseConstant.CONSOLE;
        if (ObjectUtils.isNotEmpty(sseServer.getSseEmitter(console))) {
            sseServer.sendMessage(console, "*", map);
        }
    }
}
