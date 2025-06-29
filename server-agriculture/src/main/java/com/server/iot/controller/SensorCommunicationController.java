//package com.server.iot.controller;
//
//import com.server.iot.service.SensorCommunicationService;
//import com.server.iot.service.SerialPortService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 传感器通信控制器
// * 提供传感器通信管理的API接口
// */
//@RestController
//@RequestMapping("/sensor/communication")
//public class SensorCommunicationController {
//
//    private static final Logger log = LoggerFactory.getLogger(SensorCommunicationController.class);
//
//    @Autowired
//    private SensorCommunicationService sensorCommunicationService;
//
//    @Autowired
//    private SerialPortService serialPortService;
//
//    /**
//     * 获取传感器任务状态
//     */
//    @GetMapping("/status")
//    public Map<String, Object> getSensorStatus() {
//        Map<String, Object> result = new HashMap<>();
//        result.put("sensorTasks", sensorCommunicationService.getSensorTaskStatus());
//        result.put("serialPortStatus", serialPortService.getPortStatus());
//        result.put("serialPortConnected", serialPortService.isConnected());
//        return result;
//    }
//
//    /**
//     * 重新加载传感器配置
//     */
//    @PostMapping("/reload")
//    public Map<String, Object> reloadSensorConfig() {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            sensorCommunicationService.reloadSensorConfig();
//            result.put("success", true);
//            result.put("message", "传感器配置重新加载成功");
//        } catch (Exception e) {
//            log.error("Failed to reload sensor config", e);
//            result.put("success", false);
//            result.put("message", "传感器配置重新加载失败: " + e.getMessage());
//        }
//        return result;
//    }
//
//    /**
//     * 获取串口状态
//     */
//    @GetMapping("/serial/status")
//    public Map<String, Object> getSerialPortStatus() {
//        Map<String, Object> result = new HashMap<>();
//        result.put("status", serialPortService.getPortStatus());
//        result.put("connected", serialPortService.isConnected());
//        return result;
//    }
//
//    /**
//     * 重新连接串口
//     */
//    @PostMapping("/serial/reconnect")
//    public Map<String, Object> reconnectSerialPort() {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            serialPortService.connect();
//            result.put("success", true);
//            result.put("message", "串口重新连接成功");
//        } catch (Exception e) {
//            log.error("Failed to reconnect serial port", e);
//            result.put("success", false);
//            result.put("message", "串口重新连接失败: " + e.getMessage());
//        }
//        return result;
//    }
//}
