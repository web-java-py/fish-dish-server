package com.server.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.server.domain.AgricultureDevice;
import com.server.service.AgricultureDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * 传感器通信服务
 * 负责查询传感器指令、发送指令并解析返回数据
 */
@Service
public class SensorCommunicationService {

    private static final Logger log = LoggerFactory.getLogger(SensorCommunicationService.class);

    @Autowired
    private SerialPortService serialPortService;

    @Autowired
    private DataProcessingService dataProcessingService;

    @Autowired
    private AgricultureDeviceService deviceService;

    // 线程池，用于管理传感器通信线程
    private ExecutorService executorService;
    
    // 存储正在运行的传感器任务
    private final Map<Long, Future<?>> runningTasks = new ConcurrentHashMap<>();
    
    // 串口访问锁，避免多个线程同时操作串口
    private final Object serialLock = new Object();

    @PostConstruct
    public void init() {
        log.info("正在初始化传感器通信服务...");
        executorService = Executors.newCachedThreadPool();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        log.info("应用程序已就绪，正在启动传感器通信...");
        startSensorCommunication();
    }

    @PreDestroy
    public void destroy() {
        log.info("正在关闭传感器通信服务...");
        stopAllSensorTasks();
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 启动传感器通信
     */
    private void startSensorCommunication() {
        try {
            // 查询所有传感器设备（device_type_id为1、2、6的设备  1 2 6 为传感器）
            List<AgricultureDevice> sensors = getSensorDevices();
            
            for (AgricultureDevice sensor : sensors) {
                if (sensor.getSensorCommand() != null && !"null".equals(sensor.getSensorCommand())) {
                    startSensorTask(sensor);
                } else {
                    log.warn("传感器 {} 没有配置指令", sensor.getDeviceName());
                }
            }
        } catch (Exception e) {
            log.error("启动传感器通信失败", e);
        }
    }

    /**
     * 获取传感器设备列表
     */
    private List<AgricultureDevice> getSensorDevices() {
        QueryWrapper<AgricultureDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("device_type_id", Arrays.asList("1", "2", "6"));
        return deviceService.list(queryWrapper);
    }

    /**
     * 启动单个传感器任务
     */
    private void startSensorTask(AgricultureDevice sensor) {
        if (runningTasks.containsKey(sensor.getId())) {
            log.warn("传感器 {} 的任务已在运行中", sensor.getDeviceName());
            return;
        }

        Future<?> task = executorService.submit(() -> {
            sensorRequest(sensor);
        });
        
        runningTasks.put(sensor.getId(), task);
        log.info("已启动传感器任务: {}", sensor.getDeviceName());
    }

    /**
     * 停止所有传感器任务
     */
    private void stopAllSensorTasks() {
        for (Map.Entry<Long, Future<?>> entry : runningTasks.entrySet()) {
            Future<?> task = entry.getValue();
            if (!task.isDone()) {
                task.cancel(true);
            }
        }
        runningTasks.clear();
        log.info("已停止所有传感器任务");
    }

    /**
     * 传感器请求任务
     * 周期性向对应的设备发送指令，并解析返回数据
     */
    private void sensorRequest(AgricultureDevice sensor) {
        String sensorName = sensor.getDeviceName();
        String commandHexStr = sensor.getSensorCommand();
        
        log.info("正在启动传感器请求任务: {}", sensorName);
        
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // 将16进制字符串转换为bytes
                byte[] commandBytes = hexStringToByteArray(commandHexStr);
                
                synchronized (serialLock) {
                    // 发送指令
                    serialPortService.writeToSerial(commandBytes);
                    
                    // 等待响应
                    Thread.sleep(100);
                    
                    // 读取响应数据
                    byte[] response = serialPortService.readFromSerial(256);
                    
                    if (response != null && response.length > 0) {
                        // 根据设备类型选择解析器
                        String deviceType = sensor.getDeviceTypeId();
                        Map<String, Object> parsedData = parseSensorData(response, deviceType);
                        parsedData.put("deviceId", sensor.getId());
                        parsedData.put("deviceName", sensor.getDeviceName());
                        parsedData.put("type", getDataTypeByDeviceType(deviceType));
                        
                        // 添加 pastureId 和 batchId
                        parsedData.put("pastureId", sensor.getPastureId());
                        parsedData.put("batchId", sensor.getBatchId());
                        
                        log.info("{} - {} 返回数据：{}", LocalDateTime.now(), sensorName, parsedData);
                        
                        // 将解析后的数据交给数据处理服务
                        dataProcessingService.processAndStore(parsedData);
                    } else {
                        log.warn("{} - {} 没有收到响应", LocalDateTime.now(), sensorName);
                    }
                }
                
                // 循环间隔，每隔5秒发送一次
                Thread.sleep(5000);
                
            } catch (InterruptedException e) {
                log.info("传感器 {} 的任务被中断", sensorName);
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("{} - {} 发生错误: {}", LocalDateTime.now(), sensorName, e.getMessage(), e);
                try {
                    Thread.sleep(10000); // 出错后等待10秒再重试
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
        
        log.info("传感器 {} 的任务已停止", sensorName);
    }

    /**
     * 解析传感器数据
     */
    private Map<String, Object> parseSensorData(byte[] data, String deviceType) {
        Map<String, Object> result = new HashMap<>();
        try {
            switch (deviceType) {
                case "1": // 气象传感器
                    result.putAll(parseWeatherSensorData(data));
                    break;
                case "2": // 水质传感器
                    result.putAll(parseWaterQualityData(data));
                    break;
                case "6": // 其他传感器
                    result.putAll(parseOtherSensorData(data));
                    break;
                default:
                    result.put("raw_data", bytesToHexString(data));
            }
        } catch (Exception e) {
            log.error("解析传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析气象传感器数据
     */
    private Map<String, Object> parseWeatherSensorData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (data.length >= 19) {
                // 百叶箱数据
                double humidity = Math.round(((data[3] & 0xFF) << 8 | (data[4] & 0xFF)) / 10.0 * 10.0) / 10.0;
                double temperature = Math.round(((data[5] & 0xFF) << 8 | (data[6] & 0xFF)) / 10.0 * 10.0) / 10.0;
                double noise = Math.round(((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 10.0 * 10.0) / 10.0;
                int pm25 = (data[9] & 0xFF) << 8 | (data[10] & 0xFF);
                int pm10 = (data[13] & 0xFF) << 8 | (data[14] & 0xFF);
                int light = (data[17] & 0xFF) << 8 | (data[18] & 0xFF);
                
                result.put("humidity", humidity);
                result.put("temperature", temperature);
                result.put("noise", noise);
                result.put("pm25", pm25);
                result.put("pm10", pm10);
                result.put("light_intensity", light);
            } else if (data.length >= 7) {
                // 风向或风速数据
                if (data[0] == 0x01) {
                    // 风向数据
                    int directionGrade = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
                    int directionAngle = ((data[5] & 0xFF) << 8) | (data[6] & 0xFF);
                    
                    String[] directions = {"北风", "东北风", "东风", "东南风", "南风", "西南风", "西风", "西北风"};
                    String direction = (directionGrade >= 0 && directionGrade < directions.length) 
                        ? directions[directionGrade] : "未知风向";
                    
                    result.put("wind_direction", direction);
                    result.put("direction_angle", directionAngle);
                } else if (data[0] == 0x03) {
                    // 风速数据
                    int speedValue = (data[3] & 0xFF) << 8 | (data[4] & 0xFF);
                    double speed = speedValue / 10.0;
                    result.put("wind_speed", speed);
                }
            }
        } catch (Exception e) {
            log.error("解析气象传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析水质传感器数据
     */
    private Map<String, Object> parseWaterQualityData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (data.length >= 9) {
                // 解析温度
                int tempValue = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
                int tempDecimal = ((data[5] & 0xFF) << 8) | (data[6] & 0xFF);
                double temperature = Math.round((tempValue / Math.pow(10, tempDecimal)) * Math.pow(10, tempDecimal)) / Math.pow(10, tempDecimal);
                
                // 解析pH值
                double phValue = ((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 100.0;
                
                result.put("water_temperature", temperature);
                result.put("ph_value", phValue);
            }
        } catch (Exception e) {
            log.error("解析水质传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析其他传感器数据
     */
    private Map<String, Object> parseOtherSensorData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("raw_data", bytesToHexString(data));
        } catch (Exception e) {
            log.error("解析其他传感器数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 将16进制字符串转换为字节数组
     */
    byte[] hexStringToByteArray(String hex) {
        hex = hex.replaceAll("\\s", "");
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * 将字节数组转换为16进制字符串
     */
    String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    /**
     * 解析风向传感器数据
     */
    Map<String, Object> parseWindDirectionData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (data.length >= 7) {
                int directionGrade = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
                int directionAngle = ((data[5] & 0xFF) << 8) | (data[6] & 0xFF);
                
                String[] directions = {"北风", "东北风", "东风", "东南风", "南风", "西南风", "西风", "西北风"};
                String direction = (directionGrade >= 0 && directionGrade < directions.length) 
                    ? directions[directionGrade] : "未知风向";
                
                result.put("direction_grade", directionGrade);
                result.put("direction_angle", directionAngle);
                result.put("direction", direction);
                result.put("wind_direction", direction);
            }
        } catch (Exception e) {
            log.error("解析风向数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析百叶箱数据
     */
    Map<String, Object> parseBaiyeBoxData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (data.length >= 19) {
                double humidity = Math.round(((data[3] & 0xFF) << 8 | (data[4] & 0xFF)) / 10.0 * 10.0) / 10.0;
                double temperature = Math.round(((data[5] & 0xFF) << 8 | (data[6] & 0xFF)) / 10.0 * 10.0) / 10.0;
                double noise = Math.round(((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 10.0 * 10.0) / 10.0;
                int pm25 = (data[9] & 0xFF) << 8 | (data[10] & 0xFF);
                int pm10 = (data[13] & 0xFF) << 8 | (data[14] & 0xFF);
                int light = (data[17] & 0xFF) << 8 | (data[18] & 0xFF);
                
                result.put("humidity", humidity);
                result.put("temperature", temperature);
                result.put("noise", noise);
                result.put("pm25", pm25);
                result.put("pm10", pm10);
                result.put("light", light);
                result.put("light_intensity", light);
            }
        } catch (Exception e) {
            log.error("解析百叶箱数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 解析风速传感器数据
     */
    Map<String, Object> parseWindSpeedData(byte[] data) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (data.length >= 5) {
                int speedValue = (data[3] & 0xFF) << 8 | (data[4] & 0xFF);
                double speed = speedValue / 10.0;
                result.put("speed", speed);
                result.put("wind_speed", speed);
            }
        } catch (Exception e) {
            log.error("解析风速数据出错: {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据设备类型获取数据类型
     */
    String getDataTypeByDeviceType(String deviceType) {
        switch (deviceType) {
            case "1":
                return "weather";
            case "2":
                return "water";
            case "6":
                return "other";
            default:
                return "unknown";
        }
    }

    /**
     * 重新加载传感器配置
     */
    public void reloadSensorConfig() {
        log.info("正在重新加载传感器配置...");
        stopAllSensorTasks();
        startSensorCommunication();
    }

    /**
     * 获取运行中的传感器任务状态
     */
    public Map<String, String> getSensorTaskStatus() {
        Map<String, String> status = new HashMap<>();
        for (Map.Entry<Long, Future<?>> entry : runningTasks.entrySet()) {
            Long deviceId = entry.getKey();
            Future<?> task = entry.getValue();
            String taskStatus = task.isDone() ? "STOPPED" : "RUNNING";
            status.put(deviceId.toString(), taskStatus);
        }
        return status;
    }
} 