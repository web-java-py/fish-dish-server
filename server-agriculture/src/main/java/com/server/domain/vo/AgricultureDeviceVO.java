package com.server.domain.vo;


import com.server.domain.AgricultureDevice;
import lombok.Data;

@Data
public class AgricultureDeviceVO extends AgricultureDevice {
    private String pastureName; // 大棚名称
    private String batchName;   // 分区名称
    private String deviceTypeName;  // 设备类型
}
