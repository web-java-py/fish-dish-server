package com.server.domain.vo;

import com.server.domain.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel("溯源详情返回对象")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraceabilityDetailVO {
    @ApiModelProperty("溯源食品信息")
    private AgriculturePartitionFood foodInfo;

    @ApiModelProperty("分区信息")
    private AgricultureCropBatch cropBatch;

    @ApiModelProperty("大棚信息")
    private AgriculturePasture pastureInfo;

    @ApiModelProperty("批次任务详情列表")
    private List<BatchTaskDetailVO> batchTaskDetailList;

}