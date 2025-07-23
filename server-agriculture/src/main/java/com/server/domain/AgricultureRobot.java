package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: zbb
 * @Date: 2025/7/23 16:43
 * 小农机器人聊天记录表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_robot")
@ApiModel(value = "AgricultureRobot" , description="小农聊天记录")
public class AgricultureRobot implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    @TableField(value="type")
    @ApiModelProperty(value="消息类型：bot/user")
    private String type;

    @TableField(value="content")
    @ApiModelProperty(value="消息内容")
    private String content;

    @TableField(value="timestamp")
    @ApiModelProperty(value="消息时间戳")
    private Long timestamp;

    @TableField(value="user_id")
    @ApiModelProperty(value="用户标识")
    private Long userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;
}
