//package com.server.iot.task;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.server.domain.AgricultureDevice;
//import com.server.service.AgricultureDeviceStatusService;
//import com.server.service.AgricultureDeviceService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.sql.Timestamp;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
///**
// * 设备状态定时检查任务
// *
// * @author bxwy
// */
//@Component
//public class AgricultureDeviceStatusCheckTask {
//
//    private static final Logger log = LoggerFactory.getLogger(AgricultureDeviceStatusCheckTask.class);
//
//    @Autowired
//    private AgricultureDeviceStatusService deviceStatusService;
//
//    @Autowired
//    private AgricultureDeviceService deviceService;
//
//    // 设备超时阈值（分钟）
//    private static final long OFFLINE_TIMEOUT_MINUTES = 1;
//
//    /**
//     * 定时任务：每分钟执行一次，检查所有设备是否离线
//     * 主要流程：
//     * 1. 读取每个设备的status和lastOnlineTime字段。
//     * 2. 如果设备当前为在线（status=1），且最后在线时间早于超时阈值，则调用服务将其标记为离线。
//     */
//    @Scheduled(cron = "0 */1 * * * ?")
//    public void checkDevicesOffline() {
//        log.info("开始执行定时任务：检查设备离线状态...");
//        List<AgricultureDevice> onlineDevices = deviceService.list(
//            new QueryWrapper<AgricultureDevice>()
//                .eq("status", "1")
//        );
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        for (AgricultureDevice device : onlineDevices) {
//            // 只处理 device_type_id 为 1、2、6 的设备   1 2 6为传感器
//            Set<String> validTypeIds = new HashSet<>(Arrays.asList("1", "2", "6"));
//            String typeId = device.getDeviceTypeId();
//            if (!validTypeIds.contains(typeId)) {
//                continue;
//            }
//            Date lastOnline = device.getLastOnlineTime();
//            if (lastOnline != null) {
//                LocalDateTime lastOnlineTime =
//                    new Timestamp(lastOnline.getTime()).toLocalDateTime();
//                if (lastOnlineTime.isBefore(now.minusMinutes(OFFLINE_TIMEOUT_MINUTES))) {
//                    log.info("设备 {} 已超时, 最后在线时间: {}. 标记为离线.", device.getId(), lastOnlineTime.format(formatter));
//                    deviceStatusService.updateDeviceOffline(device.getId().toString());
//                }
//            }
//        }
//        log.info("设备离线状态检查任务执行结束.");
//    }
//}
