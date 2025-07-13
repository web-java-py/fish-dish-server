package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_water_quality_hourly_stats")
@ApiModel(value = "AgricultureWaterQualityHourlyStats", description = "水质数据小时统计表")
public class AgricultureWaterQualityHourlyStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "大棚ID")
    private String pastureId;

    @ApiModelProperty(value = "分区ID")
    private String batchId;

    @ApiModelProperty(value = "统计小时")
    private LocalDateTime statHour;

    @ApiModelProperty(value = "PH值平均值")
    private BigDecimal avgPhValue;

    @ApiModelProperty(value = "溶解氧平均值")
    private BigDecimal avgDissolvedOxygen;

    @ApiModelProperty(value = "氨氮平均值")
    private BigDecimal avgAmmoniaNitrogen;

    @ApiModelProperty(value = "水温平均值")
    private BigDecimal avgWaterTemperature;

    @ApiModelProperty(value = "电导率平均值")
    private BigDecimal avgConductivity;

    @ApiModelProperty(value = "数据条数")
    private Integer dataCount;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
} 