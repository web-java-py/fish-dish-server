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
@TableName(value = "agriculture_class")
@ApiModel(value = "AgricultureClass", description = "种类信息表")
public class AgricultureClass extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "class_id", type = IdType.AUTO)
    @ApiModelProperty(value = "种类ID")
    private Long classId;

    @TableField(value = "class_name")
    @ApiModelProperty(value = "种类名称")
    private String className;

    @TableField(value = "class_type_name")
    @ApiModelProperty(value = "种类类别名称")
    private String classTypeName;

    @TableField(value = "class_type")
    @ApiModelProperty(value = "类型（0鱼 1菜）")
    private String classType;

    @TableField(value = "class_image")
    @ApiModelProperty(value = "种类图片")
    private String classImage;

    @TableField(value = "class_des")
    @ApiModelProperty(value = "宣传语")
    private String classDes;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    private Long orderNum;

}
