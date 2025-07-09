//package com.server.iot.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.server.domain.AgricultureDevice;
//import com.server.service.AgricultureDeviceService;
//import com.server.service.AgricultureDeviceStatusService;
//import com.server.service.impl.AgricultureDeviceStatusServiceImpl;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.time.LocalDateTime;
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.function.Function;
//
///**
// * 传感器通信服务
// * 核心服务，负责通过串口与传感器进行周期性的指令收发和数据解析。
// * 1. 在应用启动时，从数据库加载所有传感器设备。
// * 2. 为每个传感器创建一个独立的线程任务，进行周期性轮询。
// * 3. 发送指令给传感器，读取并解析返回的数据。
// * 4. 成功收到数据后，更新设备在Redis中的在线状态。
// * 5. 将解析后的数据交给DataProcessingService进行后续处理和存储。
// */
//@Service
//public class SensorCommunicationService {
//
//    private static final Logger log = LoggerFactory.getLogger(SensorCommunicationService.class);
//
//    @Autowired
//    private SerialPortService serialPortService; // 串口服务
//
//    @Autowired
//    private DataProcessingService dataProcessingService; // 数据处理服务，用于存储解析后的数据
//
//    @Autowired
//    private AgricultureDeviceService deviceService; // 设备信息服务，用于查询设备基础信息
//
//    @Autowired
//    private AgricultureDeviceStatusService deviceStatusService; // 设备状态服务，用于更新设备实时在线状态
//
//    // 线程池
//    private ExecutorService executorService;
//
//    // 使用ConcurrentHashMap存储每个传感器正在运行的轮询任务（Future对象）
//    private final Map<Long, Future<?>> runningTasks = new ConcurrentHashMap<>();
//
//    // 串口访问锁对象。加锁保证同一时间只有一个线程在进行读写操作，防止数据错乱。
//    private final Object serialLock = new Object();
//
//    /**
//     * 确保此方法在SensorCommunicationService的Bean初始化后立即执行。
//     * 主要用于初始化线程池。
//     */
//    @PostConstruct
//    public void init() {
//        log.info("正在初始化传感器通信服务...");
//        // 使用缓存线程池
//        executorService = Executors.newCachedThreadPool();
//    }
//
//    /**
//     * EventListener注解监听应用就绪事件（ApplicationReadyEvent）。
//     * spring boot应用完全启动并准备好接收请求时，调用此方法。
//     */
//    @EventListener(ApplicationReadyEvent.class)
//    public void onApplicationReady() {
//        log.info("应用程序已就绪，正在启动传感器通信...");
//        startSensorCommunication();
//    }
//
//    /**
//     * PreDestroy确保此方法在Spring容器销毁SensorCommunicationService的Bean之前调用。
//     * 执行资源清理工作，优雅地关闭线程池。
//     */
//    @PreDestroy
//    public void destroy() {
//        log.info("正在关闭传感器通信服务...");
//        stopAllSensorTasks(); // 先停止所有正在运行的任务
//        if (executorService != null && !executorService.isShutdown()) {
//            executorService.shutdown(); // 发起关闭命令，不再接受新任务，等待现有任务执行完毕
//            try {
//                // 等待5秒，如果线程池还未完全关闭，则强制关闭
//                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
//                    executorService.shutdownNow(); // 立即尝试停止所有正在执行的任务
//                }
//            } catch (InterruptedException e) {
//                // 如果当前线程在等待时被中断，也立即强制关闭
//                executorService.shutdownNow();
//                Thread.currentThread().interrupt(); // 重新设置中断状态
//            }
//        }
//    }
//
//    /**
//     * 启动总的传感器通信流程。
//     * 查询所有需要轮询的传感器，并逐一启动任务。
//     */
//    private void startSensorCommunication() {
//        try {
//            // 查询所有传感器设备
//            List<AgricultureDevice> sensors = getSensorDevices();
//
//            for (AgricultureDevice sensor : sensors) {
//                // 跳过未开启的设备
////                if (!"1".equals(sensor.getControlStatus())) {
////                    log.info("传感器 {} (ID: {}) 用户已关闭，跳过采集任务。", sensor.getDeviceName(), sensor.getId());
////                    continue;
////                }
//                // 跳过未配置指令的设备
//                String command = sensor.getSensorCommand();
//                if (command == null || "null".equals(command)) {
//                    log.warn("传感器 {} (ID: {}) 没有配置指令，跳过任务启动。", sensor.getDeviceName(), sensor.getId());
//                    continue;
//                }
//                // 启动任务
//                startSensorTask(sensor);
//            }
//        } catch (Exception e) {
//            log.error("启动传感器通信流程失败", e);
//        }
//    }
//
//    /**
//     * 从数据库获取所有需要轮询的传感器设备列表。
//     * @return 传感器设备列表
//     */
//    private List<AgricultureDevice> getSensorDevices() {
//        QueryWrapper<AgricultureDevice> queryWrapper = new QueryWrapper<>();
//        // '1' 气象传感器, '2' 水质传感器, '6' 其他传感器
//        queryWrapper.in("device_type_id", Arrays.asList("1", "2", "6"));
//        return deviceService.list(queryWrapper);
//    }
//
//    /**
//     * 为单个传感器设备启动一个独立的后台轮询任务。
//     * @param sensor 需要启动任务的传感器设备对象
//     */
//    private void startSensorTask(AgricultureDevice sensor) {
//        // 防止重复启动任务
//        if (runningTasks.containsKey(sensor.getId())) {
//            log.warn("传感器 {} (ID: {}) 的任务已在运行中，无需重复启动。", sensor.getDeviceName(), sensor.getId());
//            return;
//        }
//
//        // 向线程池提交一个新任务，并获取其Future对象
//        Future<?> task = executorService.submit(() -> {
//            sensorRequest(sensor);
//        });
//
//        // 将任务的Future对象存入Map中
//        runningTasks.put(sensor.getId(), task);
//        log.info("已为传感器 {} (ID: {}) 成功启动轮询任务。", sensor.getDeviceName(), sensor.getId());
//    }
//
//    /**
//     * 停止所有正在运行的传感器轮询任务。
//     * 通常在程序关闭或重新加载配置时调用。
//     */
//    private void stopAllSensorTasks() {
//        log.info("正在尝试停止所有 {} 个传感器任务...", runningTasks.size());
//        for (Map.Entry<Long, Future<?>> entry : runningTasks.entrySet()) {
//            Future<?> task = entry.getValue();
//            if (!task.isDone()) {
//                // true表示即使任务正在运行，也尝试中断它
//                task.cancel(true);
//            }
//        }
//        runningTasks.clear(); // 清空任务列表
//        log.info("已发送停止指令给所有传感器任务。");
//    }
//
//    /**
//     * 单个传感器的请求任务核心逻辑，无限循环中执行。
//     * 该方法在一个独立的线程中运行。
//     * @param sensor 当前线程负责轮询的传感器设备
//     */
//    private void sensorRequest(AgricultureDevice sensor) {
//        String sensorName = sensor.getDeviceName();
//        String commandHexStr = sensor.getSensorCommand();
//        Long sensorId = sensor.getId();
//
//        log.info("线程 {} 正在启动，负责轮询传感器: {} (ID: {})", Thread.currentThread().getName(), sensorName, sensorId);
//
//        // 使用 while(!Thread.currentThread().isInterrupted()) 作为循环条件，可以优雅地响应中断信号
//        while (!Thread.currentThread().isInterrupted()) {
//            try {
//                // 将数据库中存储的16进制指令字符串（如 "01 03 00 00 00 02 C4 0B"）转换为字节数组
//                byte[] commandBytes = hexStringToByteArray(commandHexStr);
//
//                // 使用synchronized关键字锁定serialLock对象，确保同一时间只有一个线程能访问串口
//                synchronized (serialLock) {
//                    // 1. 通过串口发送指令
//                    serialPortService.writeToSerial(commandBytes);
//
//                    // 2. 给设备留出响应时间
//                    Thread.sleep(200);
//
//                    // 3. 从串口读取响应数据，最多读取256字节
//                    byte[] response = serialPortService.readFromSerial(256);
//
//                    // 4. 检查是否收到了有效响应
//                    if (response != null && response.length > 0) {
//                        // 只要成功收到响应，将设备标记为在线
//                        deviceStatusService.updateDeviceOnline(sensor.getId().toString());
//
//                        // 5. 根据设备类型选择合适的解析器来解析数据
//                        String deviceType = sensor.getDeviceTypeId();
//                        Map<String, Object> parsedData = parseSensorData(response, deviceType);
//
//                        // 6. 将一些元数据（设备ID、名称、类型、大棚和分区ID）添加到解析结果中
//                        parsedData.put("deviceId", sensorId);
//                        parsedData.put("deviceName", sensorName);
//                        parsedData.put("type", getDataTypeByDeviceType(deviceType));
//                        parsedData.put("pastureId", sensor.getPastureId());
//                        parsedData.put("batchId", sensor.getBatchId());
//
//                        log.info("成功接收并解析来自 {} (ID: {}) 的数据: {}", sensorName, sensorId, parsedData);
//
//                        // 7. 将完整的解析数据交给数据处理服务进行下一步处理
//                        dataProcessingService.processAndStore(parsedData);
//                    } else {
//                        // 如果没有收到响应，记录警告日志。此时设备状态不会更新，后续会被定时任务标记为离线。
//                        log.warn("轮询 {} (ID: {}) 未收到响应。", sensorName, sensorId);
//                    }
//                } // synchronized代码块结束，释放锁
//
//                // 轮询间隔，本次成功或失败后，等待5秒再进行下一次轮询
//                Thread.sleep(5000);
//
//            } catch (InterruptedException e) {
//                // 当调用task.cancel(true)时，该线程会抛出InterruptedException，从而跳出循环
//                log.info("传感器 {} (ID: {}) 的轮询任务被主动中断，线程即将退出。", sensorName, sensorId);
//                Thread.currentThread().interrupt(); // 重新设置中断标志
//                break; // 退出循环
//            } catch (Exception e) {
//                // 捕获其他所有异常（如IO异常），防止任务因意外错误而终止
//                log.error("轮询传感器 {} (ID: {}) 时发生未知错误: {}", sensorName, sensorId, e.getMessage(), e);
//                try {
//                    // 发生错误后，等待更长的时间再重试，避免快速、无效的重试刷屏日志
//                    Thread.sleep(10000);
//                } catch (InterruptedException ie) {
//                    log.info("在错误等待期间，传感器 {} (ID: {}) 的任务被中断。", sensorName, sensorId);
//                    Thread.currentThread().interrupt();
//                    break;
//                }
//            }
//        }
//
//        log.info("传感器 {} (ID: {}) 的轮询任务已完全停止。", sensorName, sensorId);
//    }
//
//    /**
//     * 数据解析的主分发方法。
//     * 根据设备类型，调用相应的具体解析方法。
//     * @param data 从串口读取的原始字节数组
//     * @param deviceType 设备的类型ID ("1", "2", "6")
//     * @return 包含解析后键值对的Map
//     */
//    private Map<String, Object> parseSensorData(byte[] data, String deviceType) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            switch (deviceType) {
//                case "1": // 气象传感器
//                    result.putAll(parseWeatherSensorData(data));
//                    break;
//                case "2": // 水质传感器
//                    result.putAll(parseWaterQualityData(data));
//                    break;
//                case "6": // 其他传感器
//                    result.putAll(parseOtherSensorData(data));
//                    break;
//                default:
//                    result.put("raw_data", bytesToHexString(data));
//            }
//        } catch (Exception e) {
//            log.error("解析传感器数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 解析气象传感器数据
//     */
//    private Map<String, Object> parseWeatherSensorData(byte[] data) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            if (data.length >= 19) {
//                // 百叶箱数据
//                double humidity = Math.round(((data[3] & 0xFF) << 8 | (data[4] & 0xFF)) / 10.0 * 10.0) / 10.0;
//                double temperature = Math.round(((data[5] & 0xFF) << 8 | (data[6] & 0xFF)) / 10.0 * 10.0) / 10.0;
//                double noise = Math.round(((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 10.0 * 10.0) / 10.0;
//                int pm25 = (data[9] & 0xFF) << 8 | (data[10] & 0xFF);
//                int pm10 = (data[13] & 0xFF) << 8 | (data[14] & 0xFF);
//                int light = (data[17] & 0xFF) << 8 | (data[18] & 0xFF);
//
//                result.put("humidity", humidity);
//                result.put("temperature", temperature);
//                result.put("noise", noise);
//                result.put("pm25", pm25);
//                result.put("pm10", pm10);
//                result.put("light_intensity", light);
//            } else if (data.length >= 7) {
//                // 风向或风速数据
//                if (data[0] == 0x01) {
//                    // 风向数据
//                    int directionGrade = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
//                    int directionAngle = ((data[5] & 0xFF) << 8) | (data[6] & 0xFF);
//
//                    String[] directions = {"北风", "东北风", "东风", "东南风", "南风", "西南风", "西风", "西北风"};
//                    String direction = (directionGrade >= 0 && directionGrade < directions.length)
//                        ? directions[directionGrade] : "未知风向";
//
//                    result.put("wind_direction", direction);
//                    result.put("direction_angle", directionAngle);
//                } else if (data[0] == 0x03) {
//                    // 风速数据
//                    int speedValue = (data[3] & 0xFF) << 8 | (data[4] & 0xFF);
//                    double speed = speedValue / 10.0;
//                    result.put("wind_speed", speed);
//                }
//            }
//        } catch (Exception e) {
//            log.error("解析气象传感器数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 解析水质传感器数据
//     */
//    private Map<String, Object> parseWaterQualityData(byte[] data) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            if (data.length >= 9) {
//                // 解析温度
//                int tempValue = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
//                int tempDecimal = ((data[5] & 0xFF) << 8) | (data[6] & 0xFF);
//                double temperature = Math.round((tempValue / Math.pow(10, tempDecimal)) * Math.pow(10, tempDecimal)) / Math.pow(10, tempDecimal);
//
//                // 解析pH值
//                double phValue = ((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 100.0;
//
//                result.put("water_temperature", temperature);
//                result.put("ph_value", phValue);
//            }
//        } catch (Exception e) {
//            log.error("解析水质传感器数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 解析其他传感器数据
//     */
//    private Map<String, Object> parseOtherSensorData(byte[] data) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            result.put("raw_data", bytesToHexString(data));
//        } catch (Exception e) {
//            log.error("解析其他传感器数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 将16进制表示的字符串（例如 "01 0A"）转换为字节数组（例如 {0x01, 0x0A}）。
//     * 会自动忽略空格。
//     * @param hex 16进制字符串
//     * @return 对应的字节数组
//     */
//    byte[] hexStringToByteArray(String hex) {
//        hex = hex.replaceAll("\\s", "");
//        int len = hex.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
//                    + Character.digit(hex.charAt(i + 1), 16));
//        }
//        return data;
//    }
//
//    /**
//     * 将字节数组转换为16进制表示的字符串，每个字节后附带一个空格，便于阅读。
//     * @param bytes 字节数组
//     * @return 格式化的16进制字符串
//     */
//    String bytesToHexString(byte[] bytes) {
//        StringBuilder sb = new StringBuilder();
//        for (byte b : bytes) {
//            sb.append(String.format("%02X ", b));
//        }
//        return sb.toString().trim();
//    }
//
//    /**
//     * 解析风向传感器数据
//     */
//    Map<String, Object> parseWindDirectionData(byte[] data) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            if (data.length >= 7) {
//                int directionGrade = ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
//                int directionAngle = ((data[5] & 0xFF) << 8) | (data[6] & 0xFF);
//
//                String[] directions = {"北风", "东北风", "东风", "东南风", "南风", "西南风", "西风", "西北风"};
//                String direction = (directionGrade >= 0 && directionGrade < directions.length)
//                    ? directions[directionGrade] : "未知风向";
//
//                result.put("direction_grade", directionGrade);
//                result.put("direction_angle", directionAngle);
//                result.put("direction", direction);
//                result.put("wind_direction", direction);
//            }
//        } catch (Exception e) {
//            log.error("解析风向数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 解析百叶箱数据
//     */
//    Map<String, Object> parseBaiyeBoxData(byte[] data) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            if (data.length >= 19) {
//                double humidity = Math.round(((data[3] & 0xFF) << 8 | (data[4] & 0xFF)) / 10.0 * 10.0) / 10.0;
//                double temperature = Math.round(((data[5] & 0xFF) << 8 | (data[6] & 0xFF)) / 10.0 * 10.0) / 10.0;
//                double noise = Math.round(((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 10.0 * 10.0) / 10.0;
//                int pm25 = (data[9] & 0xFF) << 8 | (data[10] & 0xFF);
//                int pm10 = (data[13] & 0xFF) << 8 | (data[14] & 0xFF);
//                int light = (data[17] & 0xFF) << 8 | (data[18] & 0xFF);
//
//                result.put("humidity", humidity);
//                result.put("temperature", temperature);
//                result.put("noise", noise);
//                result.put("pm25", pm25);
//                result.put("pm10", pm10);
//                result.put("light", light);
//                result.put("light_intensity", light);
//            }
//        } catch (Exception e) {
//            log.error("解析百叶箱数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 解析风速传感器数据
//     */
//    Map<String, Object> parseWindSpeedData(byte[] data) {
//        Map<String, Object> result = new HashMap<>();
//        try {
//            if (data.length >= 5) {
//                int speedValue = (data[3] & 0xFF) << 8 | (data[4] & 0xFF);
//                double speed = speedValue / 10.0;
//                result.put("speed", speed);
//                result.put("wind_speed", speed);
//            }
//        } catch (Exception e) {
//            log.error("解析风速数据出错: {}", e.getMessage(), e);
//        }
//        return result;
//    }
//
//    /**
//     * 根据设备类型ID，返回一个更通用的数据分类（"weather" 或 "water"）。
//     * 这个分类用于数据处理服务和MQTT主题。
//     * @param deviceType 设备类型ID
//     * @return 数据分类字符串
//     */
//    String getDataTypeByDeviceType(String deviceType) {
//        switch (deviceType) {
//            case "1":
//                return "weather";
//            case "2":
//                return "water";
//            case "6":
//                return "other";
//            default:
//                return "unknown";
//        }
//    }
//
//    /**
//     * 公开方法，用于外部触发重新加载传感器配置。
//     * 它会先停止所有当前任务，然后重新从数据库加载并启动任务。
//     */
//    public void reloadSensorConfig() {
//        log.info("正在重新加载传感器配置...");
//        stopAllSensorTasks();
//        startSensorCommunication();
//    }
//
//    /**
//     * 获取当前所有传感器任务的运行状态。
//     * @return 一个Map，Key是设备ID，Value是任务状态（"RUNNING" 或 "STOPPED"）。
//     */
//    public Map<String, String> getSensorTaskStatus() {
//        Map<String, String> status = new HashMap<>();
//        for (Map.Entry<Long, Future<?>> entry : runningTasks.entrySet()) {
//            Long deviceId = entry.getKey();
//            Future<?> task = entry.getValue();
//            String taskStatus = task.isDone() ? "STOPPED" : "RUNNING";
//            status.put(deviceId.toString(), taskStatus);
//        }
//        return status;
//    }
//}
