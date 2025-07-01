package com.server.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.server.annotation.Excel;
import com.server.core.domain.BaseEntity;

/**
 * 设备信息对象 agriculture_device
 *
 * @author bxwy
 * @date 2025-05-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_device")
@ApiModel(value = "AgricultureDevice" , description="设备信息表")
public class AgricultureDevice extends BaseEntityPlus implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="设备ID")
    private Long id;

    /** 设备名称 */
    @TableField(value="device_name")
    @ApiModelProperty(value="设备名称")
    private String deviceName;

    /** 设备类型（传感器、控制器、网关等） */
    @TableField(value="device_image")
    @ApiModelProperty(value="设备图片URL）")
    private String deviceImage;

    /** 设备类型（传感器、控制器、网关等） */
    @TableField(value="device_type_id")
    @ApiModelProperty(value="设备类型（传感器、控制器、网关等）")
    private String deviceTypeId;

    /** 设备物理在线状态（0-离线，1-在线） */
    @TableField(value="status")
    @ApiModelProperty(value="设备状态（0-离线，1-在线）")
    private String status;

    /** 用户控制状态设备状态（0-离线，1-在线） */
    @TableField(value="control_status")
    @ApiModelProperty(value="设备状态（0-离线，1-在线）")
    private String controlStatus;

    /** 告警状态（0-正常，1-告警） */
    @TableField(value="alarm_status")
    @ApiModelProperty(value="告警状态（0-正常，1-告警）")
    private String alarmStatus;

    /** 最后在线时间 */
    @TableField(value="last_online_time")
    @ApiModelProperty(value="最后在线时间")
    private Date lastOnlineTime;

    /** 大棚id，空为没有绑定大棚 */
    @TableField(value="pasture_id")
    @ApiModelProperty(value="大棚id，空为没有绑定大棚")
    private String pastureId;

    /** 分区id，空为没有绑定分区 */
    @TableField(value="batch_id")
    @ApiModelProperty(value="分区id，空为没有绑定分区")
    private String batchId;

    /** 区块地址 */
    @TableField(value="block_address")
    @ApiModelProperty(value="合约地址")
    private String blockAddress;

    /** 传感器指令 */
    @TableField(value="sensor_command")
    @ApiModelProperty(value="传感器指令")
    private String sensorCommand;

    /** 传感器设备地址 */
    @TableField(value="sensor_address")
    @ApiModelProperty(value="传感器设备地址")
    private String sensorAddress;

    /** 打开指令 */
    @TableField(value="command_on")
    @ApiModelProperty(value="打开指令")
    private String commandOn;

    /** 关闭指令 */
    @TableField(value="command_off")
    @ApiModelProperty(value="关闭指令")
    private String commandOff;

    @TableField(value="is_controllable")
    @ApiModelProperty(value="是否可控设备")
    private String isControllable;
}