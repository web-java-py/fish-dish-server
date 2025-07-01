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
@TableName(value ="agriculture_pasture")
@ApiModel(value = "AgriculturePasture" , description="大棚")
public class AgriculturePasture extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Long id;

    @TableField(value="name")
    @ApiModelProperty(value="名称")
    private String name;

    @TableField(value="contract_addr")
    @ApiModelProperty(value="合约地址")
    private String contractAddr;

    @TableField(value="address")
    @ApiModelProperty(value="大棚位置")
    private String address;

    @TableField(value="description")
    @ApiModelProperty(value="备注")
    private String description;

    @TableField(value="area")
    @ApiModelProperty(value="大棚面积")
    private String area;

    @TableField(value="remaining_area")
    @ApiModelProperty(value="大棚剩余面积")
    private String remainingArea;

    @TableField(value="big_breeding_quantity")
    @ApiModelProperty(value="最大分区数量")
    private Long bigBreedingQuantity;

    @TableField(value="breeding_quantity")
    @ApiModelProperty(value="当前分区数量")
    private Long breedingQuantity;

    @TableField(value="del_flag")
    @ApiModelProperty(value="删除标志 (0未删除 1已删除)")
    private Long delFlag;

}
