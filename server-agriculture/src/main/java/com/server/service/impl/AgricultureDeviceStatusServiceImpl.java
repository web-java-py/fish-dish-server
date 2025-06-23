package com.server.service.impl;

import com.server.domain.AgricultureDevice;
import com.server.iot.service.SerialPortService;
import com.server.service.AgricultureDeviceService;
import com.server.service.AgricultureDeviceStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设备状态管理Service业务层处理
 *
 * @author bxwy
 */
@Service
public class AgricultureDeviceStatusServiceImpl implements AgricultureDeviceStatusService {

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;

    private static final Logger log = LoggerFactory.getLogger(AgricultureDeviceService.class);


    //在线
    @Override
    public void updateDeviceOnline(String deviceId) {
        AgricultureDevice deviceToUpdate = new AgricultureDevice();
        Long id = Long.parseLong(deviceId);
        // 查询设备详情
        AgricultureDevice device = agricultureDeviceService.selectAgricultureDeviceById(String.valueOf(id));
        if (device != null) {
            log.info("设备 {} ({}) 上线, 当前时间: {}", device.getId(), device.getDeviceName(), new Date());
        }
        deviceToUpdate.setId(id);
        deviceToUpdate.setStatus("1");
        deviceToUpdate.setLastOnlineTime(new java.util.Date());
        agricultureDeviceService.updateAgricultureDevice(deviceToUpdate);
    }

    // 离线
    @Override
    public void updateDeviceOffline(String deviceId) {
        AgricultureDevice deviceToUpdate = new AgricultureDevice();
        deviceToUpdate.setId(Long.parseLong(deviceId));
        deviceToUpdate.setStatus("0");
        agricultureDeviceService.updateAgricultureDevice(deviceToUpdate);
    }

}