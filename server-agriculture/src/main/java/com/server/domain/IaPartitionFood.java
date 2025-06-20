package com.server.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "采摘信息")
@Data
public class IaPartitionFood implements Serializable {

    @ApiModelProperty(value = "id-溯源码", example = "123")
    private String id;

    @ApiModelProperty(value = "分区id", example = "123", required = true)
    @NotBlank(message = "分区id为空")
    private String iaPartitionId;

    @ApiModelProperty(value = "日期", required = true)
    @NotNull(message = "日期为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;


    @ApiModelProperty(value = "食品名称", example = "生菜", required = true)
    @NotBlank(message = "食品名称为空")
    private String name;
    //重量
    @ApiModelProperty(value = "重量", example = "12322", required = true)
    @NotNull(message = "重量为空")
    private BigDecimal weight;

    @ApiModelProperty(value = "食品质量（0 不及格、1 及格 2 优秀）", example = "1", required = true)
    @NotNull(message = "食品质量为空")
    private Integer status;
    //备注
    @ApiModelProperty(value = "备注", example = "test", required = true)
    @NotBlank(message = "备注为空")
    private String description;

    @ApiModelProperty(value = "二维码", example = "test")
    @TableField(exist = false)
    private String barcode;



}
