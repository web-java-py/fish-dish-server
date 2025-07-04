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
@TableName(value ="agriculture_water_quality_data")
@ApiModel(value = "AgricultureWaterQualityData" , description="水质数据表")
public class AgricultureWaterQualityData  implements Serializable  {

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

    @TableField(value="ph_value")
    @ApiModelProperty(value="PH值")
    private Double phValue;

    @TableField(value="dissolved_oxygen")
    @ApiModelProperty(value="溶解氧(mg/L)")
    private Double dissolvedOxygen;

    @TableField(value="ammonia_nitrogen")
    @ApiModelProperty(value="氨氮(mg/L)")
    private Double ammoniaNitrogen;

    @TableField(value="water_temperature")
    @ApiModelProperty(value="水温(℃)")
    private Double waterTemperature;

    @TableField(value="conductivity")
    @ApiModelProperty(value="电导率(μS/cm)")
    private Double conductivity;

    @TableField(value="collect_time")
    @ApiModelProperty(value="采集时间")
    private LocalDateTime collectTime;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

}
