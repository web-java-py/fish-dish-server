package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: zbb
 * @Date: 2025/5/23 16:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_machine")
@ApiModel(value = "AgricultureMachine",description = "机械信息表")
public class AgricultureMachine implements Serializable{

    private static final long serialVersionUID = 1L;


    @TableId(value = "machine_id" ,type = IdType.AUTO)
    @ApiModelProperty(value = "机械ID")
    private Long machineId;


    @TableField(value = "machine_code")
    @ApiModelProperty(value = "机械编码")
    private String machineCode;


    @TableField(value = "machine_name")
    @ApiModelProperty(value = "机械名称")
    private String machineName;


    @TableField(value="machine_image")
    @ApiModelProperty(value="机械图片")
    private String machineImage;

    @TableField(value = "measure_unit")
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private String status;


    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    private Long orderNum;

    @TableField(value = "create_by",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者ID")
    private String createBy;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_by",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(value = "del_flag")
    @ApiModelProperty(value = "** 删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @TableField(exist = false)
    private Map<String, Object> params;
}