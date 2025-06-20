package com.server.iot.service;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SerialPortService {

    private static final Logger log = LoggerFactory.getLogger(SerialPortService.class);

    @Value("${serial.port-name}")
    private String portName;

    @Value("${serial.baud-rate}")
    private int baudRate;

    @Autowired
    private DataProcessingService dataProcessingService;

    private SerialPort commPort;

    @PostConstruct
    public void init() {
        log.info("Initializing Serial Port Service...");
        connect();
    }

    public void connect() {
        log.info("Attempting to connect to serial port: {} with baud rate: {}", portName, baudRate);
        commPort = SerialPort.getCommPort(portName);
        commPort.setBaudRate(baudRate);

        if (commPort.openPort()) {
            log.info("Successfully opened port: {}", portName);
            commPort.addDataListener(new SerialPortDataListener() {
                @Override
                public int getListeningEvents() {
                    return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                }

                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                        return;
                    }
                    byte[] newData = new byte[commPort.bytesAvailable()];
                    int numRead = commPort.readBytes(newData, newData.length);
                    log.debug("Read {} bytes from serial port.", numRead);
                    // 将读取到的数据交给数据处理服务
                    dataProcessingService.processAndStore(newData);
                }
            });
        } else {
            log.error("Failed to open port: {}. Please check port name and permissions.", portName);
            log.info("Available ports: {}", listPorts());
        }
    }

    @PreDestroy
    public void disconnect() {
        if (commPort != null && commPort.isOpen()) {
            if (commPort.closePort()) {
                log.info("Successfully closed port: {}", portName);
            } else {
                log.error("Failed to close port: {}", portName);
            }
        }
    }

    public List<String> listPorts() {
        return Arrays.stream(SerialPort.getCommPorts())
                .map(SerialPort::getSystemPortName)
                .collect(Collectors.toList());
    }
} 