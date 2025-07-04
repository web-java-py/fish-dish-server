//package com.server.iot.service;
//
//import com.fazecast.jSerialComm.SerialPort;
//import com.fazecast.jSerialComm.SerialPortDataListener;
//import com.fazecast.jSerialComm.SerialPortEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class SerialPortService {
//
//    private static final Logger log = LoggerFactory.getLogger(SerialPortService.class);
//
//    @Value("${serial.port-name}")
//    private String portName;
//
//    @Value("${serial.baud-rate}")
//    private int baudRate;
//
//    @Autowired
//    private DataProcessingService dataProcessingService;
//
//    private SerialPort commPort;
//
//    @PostConstruct
//    public void init() {
//        log.info("正在初始化串口服务...");
//        connect();
//    }
//
//    public void connect() {
//        log.info("正在尝试连接串口: {}，波特率: {}", portName, baudRate);
//        commPort = SerialPort.getCommPort(portName);
//        commPort.setBaudRate(baudRate);
//
//        if (commPort.openPort()) {
//            log.info("成功打开串口: {}", portName);
//        } else {
//            log.error("打开串口失败: {}。请检查串口名称和权限。", portName);
//            log.info("可用串口: {}", listPorts());
//        }
//    }
//
//    @PreDestroy
//    public void disconnect() {
//        if (commPort != null && commPort.isOpen()) {
//            if (commPort.closePort()) {
//                log.info("成功关闭串口: {}", portName);
//            } else {
//                log.error("关闭串口失败: {}", portName);
//            }
//        }
//    }
//
//    public List<String> listPorts() {
//        return Arrays.stream(SerialPort.getCommPorts())
//                .map(SerialPort::getSystemPortName)
//                .collect(Collectors.toList());
//    }
//
//    /**
//     * 向串口写入数据
//     * @param data 要写入的数据
//     * @return 写入的字节数
//     */
//    public int writeToSerial(byte[] data) {
//        if (commPort != null && commPort.isOpen()) {
//            int bytesWritten = commPort.writeBytes(data, data.length);
//            log.debug("向串口写入 {} 字节", bytesWritten);
//            return bytesWritten;
//        } else {
//            log.error("串口未打开");
//            return -1;
//        }
//    }
//
//    /**
//     * 从串口读取数据
//     * @param maxBytes 最大读取字节数
//     * @return 读取的数据
//     */
//    public byte[] readFromSerial(int maxBytes) {
//        if (commPort != null && commPort.isOpen()) {
//            byte[] data = new byte[maxBytes];
//            int bytesRead = commPort.readBytes(data, Math.min(maxBytes, commPort.bytesAvailable()));
//            if (bytesRead > 0) {
//                byte[] result = new byte[bytesRead];
//                System.arraycopy(data, 0, result, 0, bytesRead);
//                log.debug("从串口读取 {} 字节", bytesRead);
//                return result;
//            }
//        } else {
//            log.error("串口未打开");
//        }
//        return new byte[0];
//    }
////
//    /**
//     * 检查串口是否已连接
//     * @return 是否已连接
//     */
//    public boolean isConnected() {
//        return commPort != null && commPort.isOpen();
//    }
//
//    /**
//     * 获取串口状态信息
//     * @return 状态信息
//     */
//    public String getPortStatus() {
//        if (commPort == null) {
//            return "NOT_INITIALIZED";
//        }
//        if (commPort.isOpen()) {
//            return "OPEN";
//        } else {
//            return "CLOSED";
//        }
//    }
//}
