package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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
@TableName(value ="agriculture_auto_control_strategy")
@ApiModel(value = "AgricultureAutoControlStrategy" , description="设备自动调节策略表")
public class AgricultureAutoControlStrategy implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键，自增")
    private Long id;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="大棚ID")
    private String pastureId;

    @TableField(value="batch_id")
    @ApiModelProperty(value="分区ID")
    private String batchId;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID")
    private String deviceId;

    @TableField(value="strategy_type")
    @ApiModelProperty(value="策略类型（weather=气象，water=水质）")
    private String strategyType;

    @TableField(value="parameter")
    @ApiModelProperty(value="监测参数（如 temperature）")
    private String parameter;

    @TableField(value = "condition_operator")
    @ApiModelProperty(value = "触发条件操作符")
    private String conditionOperator;

    @TableField(value = "condition_value")
    @ApiModelProperty(value = "触发条件数值")
    private BigDecimal conditionValue;

    @TableField(value = "execute_duration")
    @ApiModelProperty(value = "执行时长(秒)")
    private Integer executeDuration;

    @TableField(value="action")
    @ApiModelProperty(value="执行动作（如 on/off）")
    private String action;

    @TableField(value="status")
    @ApiModelProperty(value="启用状态（1启用，0禁用）")
    private Long status;

    @TableField(value="description")
    @ApiModelProperty(value="策略说明")
    private String description;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

}
