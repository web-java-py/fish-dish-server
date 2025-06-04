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
 * @author 851543
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_crop_batch")
@ApiModel(value = "AgricultureCropBatch" , description="分区表")
public class AgricultureCropBatch extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value = "batch_id", type = IdType.AUTO)
    @ApiModelProperty(value="分区ID")
    private Long batchId;

    @TableField(value="batch_name")
    @ApiModelProperty(value="分区名称")
    private String batchName;

    @TableField(value="germplasm_id")
    @ApiModelProperty(value="种质ID")
    private Long germplasmId;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="大棚ID")
    private Long pastureId;

    @TableField(value="crop_area")
    @ApiModelProperty(value="种植面积(亩)")
    private Double cropArea;

    @TableField(value="start_time")
    @ApiModelProperty(value="开始时间")
    private LocalDateTime startTime;

    @TableField(value="status")
    @ApiModelProperty(value="状态")
    private String status;

    @TableField(value="order_num")
    @ApiModelProperty(value="排序")
    private Long orderNum;

    @TableField(value="del_flag")
    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;

    @TableField(value="responsible_person_id")
    @ApiModelProperty(value="负责人Id")
    private Long responsiblePersonId;

    @TableField(value="contract_addr")
    @ApiModelProperty(value="区块链合约地址")
    private String contractAddr;

}
