package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value = "溯源查询记录")
@Data
@TableName(value = "agriculture_traceability_log")
public class AgricultureTraceabilityLog implements Serializable {

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "溯源码", required = true)
    @TableField(value = "trace_code")
    private String traceCode;

    @ApiModelProperty(value = "分区ID", required = true)
    @TableField(value = "partition_id")
    private String partitionId;

    @ApiModelProperty(value = "查询时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @TableField(value = "query_time")
    private Date queryTime;

    @ApiModelProperty(value = "查询IP")
    @TableField(value = "query_ip")
    private String queryIp;

    @ApiModelProperty(value = "用户代理")
    @TableField(value = "user_agent")
    private String userAgent;

    @ApiModelProperty(value = "查询类型(web/app/scan)")
    @TableField(value = "query_type")
    private String queryType;

    @ApiModelProperty(value = "食品类型(fish-鱼, cuisine-蔬菜)", required = true)
    @TableField(value = "food_type")
    private String foodType;
}