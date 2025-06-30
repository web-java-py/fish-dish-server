package com.server.aspect;

import com.server.annotation.SeeRefreshData;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureConsoleService;
import com.server.service.TotalService;
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
public class DataSeeAspect {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private TotalService totalService;

    @After("@annotation(seeRefreshData)")
    public void after(SeeRefreshData seeRefreshData) {
        if (seeRefreshData.seeMessageType() == SeeMessageType.Data || seeRefreshData.seeMessageType() == SeeMessageType.CONSOLE_AND_DATA) {
            Map<String, Object> map = totalService.listTotal();
            String name = SeeMessageType.Data.name();
            if (ObjectUtils.isNotEmpty(sseServer.getSseEmitter(name))) {
                sseServer.sendMessage(name, name, map);
            }
        }
    }
}
