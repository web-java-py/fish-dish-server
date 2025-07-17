package com.server.aspect;

import com.server.annotation.SeeRefreshData;
import com.server.constant.Constants;
import com.server.enums.SeeMessageType;
import com.server.service.ScaleService;
import com.server.service.TraceTotalService;
import com.server.sse.SseServer;
import dev.langchain4j.agent.tool.P;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class DataSeeAspect {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private ScaleService scaleService;

    @Autowired
    private TraceTotalService traceTotalService;

    @After("@annotation(seeRefreshData)")
    public void after(SeeRefreshData seeRefreshData) {
        if (seeRefreshData.seeMessageType() == SeeMessageType.DATA || seeRefreshData.seeMessageType() == SeeMessageType.ALL) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("device", scaleService.listDevice());
            hashMap.put("agriculture",  scaleService.listAgriculture());
            hashMap.put("traceTotal",  traceTotalService.getTraceTotal());
            sseServer.sendToAllMessage(SeeMessageType.DATA.name(), hashMap);
        }
    }
}
