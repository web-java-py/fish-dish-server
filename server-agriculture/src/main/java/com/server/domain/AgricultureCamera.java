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
 * @author bxwy
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_camera")
@ApiModel(value = "AgricultureCamera" , description="摄像头参数表")
public class AgricultureCamera extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    private Long id;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID，关联agriculture_device表")
    private Long deviceId;

    @TableField(value="username")
    @ApiModelProperty(value="摄像头登录用户名")
    private String username;

    @TableField(value="password")
    @ApiModelProperty(value="摄像头登录密码")
    private String password;

    @TableField(value="ip")
    @ApiModelProperty(value="摄像头IP地址")
    private String ip;

    @TableField(value="port")
    @ApiModelProperty(value="摄像头端口号")
    private Long port;

    @TableField(value="channel")
    @ApiModelProperty(value="通道号")
    private Long channel;

    @TableField(value="subtype")
    @ApiModelProperty(value="码流类型（0-主码流，1-子码流）")
    private Long subtype;

}
