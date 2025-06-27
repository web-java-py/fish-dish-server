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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/** 
 * @author bcwy
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_device_mqtt_config")
@ApiModel(value = "AgricultureDeviceMqttConfig" , description="设备MQTT配置表")
public class AgricultureDeviceMqttConfig extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    private Long id;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID")
    private Long deviceId;

    @TableField(value="mqtt_broker")
    @ApiModelProperty(value="MQTT Broker地址")
    private String mqttBroker;

    @TableField(value="mqtt_topic")
    @ApiModelProperty(value="订阅主题")
    private String mqttTopic;

    @TableField(value="mqtt_qos")
    @ApiModelProperty(value="QOS等级")
    private Long mqttQos;

    @TableField(value="mqtt_username")
    @ApiModelProperty(value="MQTT用户名")
    private String mqttUsername;

    @TableField(value="mqtt_password")
    @ApiModelProperty(value="MQTT密码")
    private String mqttPassword;

}
