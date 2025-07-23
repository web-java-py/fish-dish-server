package com.server.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "采摘信息")
@Data
@TableName(value = "agriculture_partition_food")
public class AgriculturePartitionFood implements Serializable {

    @ApiModelProperty(value = "id-溯源码", example = "123")
    @TableField(value = "id")
    private String id;

    @ApiModelProperty(value = "分区id", example = "123", required = true)
    @NotBlank(message = "分区id为空")
    @TableField(value = "ia_partition_id")
    private String iaPartitionId;

    @ApiModelProperty(value = "日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "date")
    private Date date;


    @ApiModelProperty(value = "食品名称", example = "生菜")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "蔬菜重量", example = "12322")
    @TableField(value = "cuisine_weight")
    private Double cuisineWeight;

    @ApiModelProperty(value = "鱼类重量", example = "12322")
    @TableField(value = "fish_weight")
    private Double fishWeight;

    @ApiModelProperty(value = "食品质量（0 不及格、1 及格 2 优秀）", example = "1")
    @TableField(value = "status")
    private Integer status;

    @ApiModelProperty(value = "蔬菜食品质量（0 不及格、1 及格 2 优秀）")
    @TableField(value = "cuisine_status")
    private Integer cuisineStatus;

    @ApiModelProperty(value = "鱼类食品质量（0 不及格、1 及格 2 优秀）")
    @TableField(value = "fish_status")
    private Integer fishStatus;

    @ApiModelProperty(value = "食品类型（fish-鱼 cuisine-菜）")
    @TableField(value = "food_type")
    private String foodType;
    //备注
    @ApiModelProperty(value = "备注", example = "test")
    @TableField(value = "description")
    private String description;

    @ApiModelProperty(value = "二维码", example = "test")
    @TableField(exist = false)
    private String barcode;

    @ApiModelProperty(value = "首次溯源查询时间", example = "2025-07-17 20:27:48")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "first_trace_time")
    private Date firstTraceTime;
}
