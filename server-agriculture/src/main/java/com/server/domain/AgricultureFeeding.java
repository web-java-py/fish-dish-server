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

@ApiModel(value = "施肥")
@Data
public class AgricultureFeeding implements Serializable {
    private String id;

    @ApiModelProperty(value = "分区id", example = "123", required = true)
    @NotBlank(message = "分区id为空")
    private String iaPartitionId;

    @ApiModelProperty(value = "施肥时间", required = true)
    @NotNull(message = "施肥时间为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date date;

    @ApiModelProperty(value = "施肥名称", example = "生菜", required = true)
    @NotBlank(message = "施肥名称为空")
    private String foodName;
    //重量
    @ApiModelProperty(value = "重量", example = "12322", required = true)
    @NotNull(message = "重量为空")
    private BigDecimal weight;
    //备注
    @ApiModelProperty(value = "备注", example = "test", required = true)
    @NotBlank(message = "备注为空")
    private String description;

    @ApiModelProperty(value = "分区名", example = "test")
    @TableField(exist = false)
    private String partitionName;


}
