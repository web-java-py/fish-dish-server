package com.server.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;


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
public class AgricultureCropBatch implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "batch_id", type = IdType.AUTO)
    @ApiModelProperty(value="分区ID")
    private Long batchId;

    @TableField(value="batch_name")
    @ApiModelProperty(value="分区名称")
    private String batchName;

    @TableField(value="germplasm_id")
    @ApiModelProperty(value="鱼种质ID")
    private Long germplasmId;

    @TableField(value="vegetable_id")
    @ApiModelProperty(value="菜种质ID")
    private Long vegetableId;

    @TableField(value="pasture_id")
    @ApiModelProperty(value="大棚ID")
    private Long pastureId;

    @TableField(value="fish_area")
    @ApiModelProperty(value="养殖面积(亩)")
    private Double fishArea;

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

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者ID")
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value="contract_addr")
    @ApiModelProperty(value="区块链合约地址")
    private String contractAddr;

    @TableField(value="harvest")
    @ApiModelProperty(value="收获标志(0代表已收获，1代表未收获)")
    private String harvest;
}
