package com.server.ai.tool;

import dev.langchain4j.agent.tool.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AgricultureTools {
    @Autowired
    private DeviceControlClient deviceControlClient;

    @Tool("启动电动推杆")
    public String startPushRod() {
        return deviceControlClient.controlDevice(DeviceEnum.PUSH_ROD.buildRequest("on"));
    }

    @Tool("关闭电动推杆")
    public String stopPushRod() {
        System.out.println("88888888888888888888");
        return deviceControlClient.controlDevice(DeviceEnum.PUSH_ROD.buildRequest("off"));
    }

    @Tool("启动排气扇")
    public String startFan() {
        return deviceControlClient.controlDevice(DeviceEnum.FAN.buildRequest("on"));
    }

    @Tool("关闭排气扇")
    public String stopFan() {
        return deviceControlClient.controlDevice(DeviceEnum.FAN.buildRequest("off"));
    }

    @Tool("启动警报灯")
    public String startAlarmLight() {
        return deviceControlClient.controlDevice(DeviceEnum.ALARM_LIGHT.buildRequest("on"));
    }

    @Tool("关闭警报灯")
    public String stopAlarmLight() {
        return deviceControlClient.controlDevice(DeviceEnum.ALARM_LIGHT.buildRequest("off"));
    }
}
