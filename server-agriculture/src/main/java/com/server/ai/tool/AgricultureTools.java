package com.server.ai.tool;

import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AgricultureTools {
    @Autowired
    private DeviceControlClient deviceControlClient;

    @Tool("启动电动推杆")
    public String startPushRod(String bot) {
        log.info("{}启动电动推杆", bot);
        return deviceControlClient.controlDevice(DeviceEnum.PUSH_ROD.buildRequest("on"));
    }

    @Tool("关闭电动推杆")
    public String stopPushRod(String bot) {
        log.info("{}关闭电动推杆", bot);
        return deviceControlClient.controlDevice(DeviceEnum.PUSH_ROD.buildRequest("off"));
    }

    @Tool("启动排气扇")
    public String startFan(String bot) {
        log.info("{}启动排气扇", bot);
        return deviceControlClient.controlDevice(DeviceEnum.FAN.buildRequest("on"));
    }

    @Tool("关闭排气扇")
    public String stopFan(String bot) {
        log.info("{}关闭排气扇", bot);
        return deviceControlClient.controlDevice(DeviceEnum.FAN.buildRequest("off"));
    }

    @Tool("启动警报灯")
    public String startAlarmLight(String bot) {
        log.info("{}启动警报灯", bot);
        return deviceControlClient.controlDevice(DeviceEnum.ALARM_LIGHT.buildRequest("on"));
    }

    @Tool("关闭警报灯")
    public String stopAlarmLight(String bot) {
        log.info("{}关闭警报灯", bot);
        return deviceControlClient.controlDevice(DeviceEnum.ALARM_LIGHT.buildRequest("off"));
    }
}
