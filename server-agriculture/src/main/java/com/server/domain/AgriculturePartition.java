package com.server.domain;
//package org.zh.blockchain.insure.model.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @ Description   :  分区实体类
 * @ Author        :  morton
 * @ CreateDate    :  2024/10/13 16:56
 * @ Version       :  1.0
 */

@Data
@ApiModel(value = "分区")
public class AgriculturePartition {

    @ApiModelProperty(value = "分区id")
    private String id;

    @ApiModelProperty(value = "大棚", example = "1234567890123456789" , required = true)
    @NotBlank(message = "所属大棚不能为空")
    private String pastureId;

    @ApiModelProperty(value = "合约地址", example = "0x123456789abcdef")
    private String contractAddr;

    @ApiModelProperty(value = "品种", example = "白菜" , required = true)
    @NotBlank(message = "种类不能为空")
    private String variety;

    @ApiModelProperty(value = "种植名称", example = "白菜", required = true)
    @NotNull(message = "种植名称不能为空")
    private String plantName;

    @ApiModelProperty(value = "分区名称", example = "1分区", required = true)
    @NotNull(message = "分区名称不能为空")
    private String partitionName;

    @ApiModelProperty(value = "成熟时间", example = "2022-01-01T00:00:00")
    private Date harvestDate;

    @ApiModelProperty(value = "种植日期", example = "2022-01-01T00:00:00")
    private Date plantDate;

    @ApiModelProperty(value = "是否加工", example = "是否加工，0未加工1加工中2加工完成")
    private Integer processState;

    @ApiModelProperty(value = "数量", example = "1")
    private Integer number;

    @ApiModelProperty(value = "成熟状态", example = "成熟状态，0未成熟，1成熟")
    private Integer status;

    @ApiModelProperty(value = "加工日期", example = "2022-01-01T00:00:00")
    private Date processDate;

    @ApiModelProperty(value = "备注", example = "无")
    private String remark;

    @ApiModelProperty(hidden = true)
    private Date updateTime;

    @ApiModelProperty(hidden = true)
    private Date createTime;
}
