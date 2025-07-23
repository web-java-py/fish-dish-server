package com.server.aspect;

import com.server.annotation.SeeRefreshData;
import com.server.constant.Constants;
import com.server.enums.SeeMessageType;
import com.server.service.AgricultureConsoleService;
import com.server.sse.SseServer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class AgricultureSeeAspect {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private AgricultureConsoleService agricultureConsoleService;

    @After("@annotation(seeRefreshData)")
    public void after(SeeRefreshData seeRefreshData) {
        if (seeRefreshData.seeMessageType() == SeeMessageType.AGRICULTURE || seeRefreshData.seeMessageType() == SeeMessageType.ALL) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("agriculture", agricultureConsoleService.listAgriculture());
            hashMap.put("batchTask", agricultureConsoleService.listBatchTask());
            hashMap.put("traceTotal",agricultureConsoleService.listTraceTotal());
            sseServer.sendToAllMessage(SeeMessageType.AGRICULTURE.name(), hashMap);
        }
    }
}
