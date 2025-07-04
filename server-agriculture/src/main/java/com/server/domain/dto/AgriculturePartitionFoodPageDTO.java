package com.server.domain.dto;/*
 * @author 不羡晚吟
 * @version 1.0
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "分页分区食品查询")
@Data
public class AgriculturePartitionFoodPageDTO {
    @ApiModelProperty(value = "分区id", example = "2341", required = true)
    private String partitionId;
}
