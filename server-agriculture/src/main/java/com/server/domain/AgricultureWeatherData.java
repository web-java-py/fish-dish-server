package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
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
@TableName(value ="agriculture_weather_data")
@ApiModel(value = "AgricultureWeatherData" , description="气象数据表")
public class AgricultureWeatherData implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    private Long id;

    @TableField(value="device_id")
    @ApiModelProperty(value="设备ID")
    private Long deviceId;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="大棚ID")
    private String pastureId;

    @TableField(value="batch_id")
    @ApiModelProperty(value="分区ID")
    private String batchId;

    @TableField(value="wind_speed")
    @ApiModelProperty(value="风速(m/s)")
    private Double windSpeed;

    @TableField(value="wind_direction")
    @ApiModelProperty(value="风向")
    private String windDirection;

    @TableField(value="temperature")
    @ApiModelProperty(value="温度(℃)")
    private Double temperature;

    @TableField(value="humidity")
    @ApiModelProperty(value="湿度(%)")
    private Double humidity;

    @TableField(value="light_intensity")
    @ApiModelProperty(value="光照强度(lux)")
    private Double lightIntensity;

    @TableField(value="rainfall")
    @ApiModelProperty(value="降雨量(mm)")
    private Double rainfall;

    @TableField(value="air_pressure")
    @ApiModelProperty(value="气压(hPa)")
    private Double airPressure;

    @TableField(value = "collect_time")
    @ApiModelProperty(value = "采集时间")
    private LocalDateTime collectTime;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;
}
