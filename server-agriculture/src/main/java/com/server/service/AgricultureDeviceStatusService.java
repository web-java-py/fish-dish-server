package com.server.service;

import java.util.Map;
import java.util.List;

/**
 * 设备状态管理Service接口
 * 负责处理设备在Redis中的实时状态
 *
 * @author Gemini
 */
public interface AgricultureDeviceStatusService {

    /**
     * 更新设备为在线状态
     * 当接收到设备心跳或数据时调用
     *
     * @param deviceId 设备ID
     */
    void updateDeviceOnline(String deviceId);

    /**
     * 更新设备为离线状态
     * 当定时任务检测到设备超时后调用
     *
     * @param deviceId 设备ID
     */
    void updateDeviceOffline(String deviceId);

}