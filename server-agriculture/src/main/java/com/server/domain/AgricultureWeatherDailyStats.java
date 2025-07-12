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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_weather_daily_stats")
@ApiModel(value = "AgricultureWeatherDailyStats", description = "气象数据日统计表")
public class AgricultureWeatherDailyStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "大棚ID")
    private String pastureId;

    @ApiModelProperty(value = "分区ID")
    private String batchId;

    @ApiModelProperty(value = "统计日期")
    private LocalDate statDate;

    @ApiModelProperty(value = "平均温度")
    private BigDecimal avgTemperature;

    @ApiModelProperty(value = "平均湿度")
    private BigDecimal avgHumidity;

    @ApiModelProperty(value = "平均风速")
    private BigDecimal avgWindSpeed;

    @ApiModelProperty(value = "平均光照强度")
    private BigDecimal avgLightIntensity;

    @ApiModelProperty(value = "平均降雨量")
    private BigDecimal avgRainfall;

    @ApiModelProperty(value = "平均气压")
    private BigDecimal avgAirPressure;

    @ApiModelProperty(value = "数据条数")
    private Integer dataCount;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}