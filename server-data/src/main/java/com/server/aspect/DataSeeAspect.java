package com.server.aspect;

import com.server.annotation.SeeRefreshData;
import com.server.constant.Constants;
import com.server.enums.SeeMessageType;
import com.server.service.ScaleService;
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
public class DataSeeAspect {

    @Autowired
    private SseServer sseServer;

    @Autowired
    private ScaleService scaleService;

    @After("@annotation(seeRefreshData)")
    public void after(SeeRefreshData seeRefreshData) {
        if (seeRefreshData.seeMessageType() == SeeMessageType.DATA || seeRefreshData.seeMessageType() == SeeMessageType.ALL) {
            Map<String, Object> hashMap = new HashMap<>();
            hashMap.put("device", scaleService.listDevice());
            hashMap.put("agriculture",  scaleService.listAgriculture());
            sseServer.sendMessage(Constants.ALL_USER, SeeMessageType.DATA.name(), hashMap);
        }
    }
}
