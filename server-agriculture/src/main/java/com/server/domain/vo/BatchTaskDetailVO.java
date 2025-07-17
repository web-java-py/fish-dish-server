package com.server.domain.vo;

import com.server.domain.AgricultureBatchTask;
import com.server.domain.AgricultureDeviceSensorAlert;
import com.server.domain.AgricultureWaterQualityData;
import com.server.domain.AgricultureWeatherData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@ApiModel("批次任务详情对象")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchTaskDetailVO {

    @ApiModelProperty("批次任务信息")
    private AgricultureBatchTask batchTask;

//    @ApiModelProperty("原始气象数据列表")
//    private List<AgricultureWeatherData> weatherDataList;

    @ApiModelProperty("每3条聚合后的气象数据列表")
    private List<AgricultureWeatherData> weatherMergedList;

    @ApiModelProperty("气象数据平均值")
    private Map<String, Double> weatherAvg;

    @ApiModelProperty("原始水质数据列表")
    private List<AgricultureWaterQualityData> waterQualityDataList;

    @ApiModelProperty("水质数据平均值")
    private Map<String, Double> waterQualityAvg;

    @ApiModelProperty("该批次任务区间内的传感器预警信息")
    private List<AgricultureDeviceSensorAlert> sensorAlertList;

    @ApiModelProperty("该批次任务区间内的预警条数")
    private Integer alertCount;
}