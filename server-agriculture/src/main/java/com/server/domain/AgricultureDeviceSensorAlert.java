package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/** 
 * @author 851543
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_device_sensor_alert")
@ApiModel(value = "AgricultureDeviceSensorAlert" , description="传感器预警信息表")
public class AgricultureDeviceSensorAlert extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="预警ID")
    private Long id;

    @TableField(value="alert_type")
    @ApiModelProperty(value="预警类型")
    private String alertType;

    @TableField(value="alert_message")
    @ApiModelProperty(value="预警消息")
    private String alertMessage;

    @TableField(value="param_name")
    @ApiModelProperty(value="参数名称")
    private String paramName;

    @TableField(value="param_value")
    @ApiModelProperty(value="参数值")
    private String paramValue;

    @TableField(value="threshold_min")
    @ApiModelProperty(value="阈值下限")
    private Double thresholdMin;

    @TableField(value="threshold_max")
    @ApiModelProperty(value="阈值上限")
    private Double thresholdMax;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="大棚ID")
    private String pastureId;

    @TableField(value="batch_id")
    @ApiModelProperty(value="分区ID")
    private String batchId;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID")
    private Long deviceId;

    @TableField(value="device_name")
    @ApiModelProperty(value="设备名称")
    private String deviceName;

    @TableField(value="device_type")
    @ApiModelProperty(value="设备类型")
    private String deviceType;

    @TableField(value="block_address")
    @ApiModelProperty(value="合约地址")
    private String blockAddress;

    @TableField(value="alert_time")
    @ApiModelProperty(value="预警时间")
    private LocalDateTime alertTime;

    @TableField(value="alert_level")
    @ApiModelProperty(value="级别（0-警告，1-严重）")
    private Long alertLevel;

    @TableField(value="status")
    @ApiModelProperty(value="处理状态（0未处理，1已处理）")
    private Long status;

}
