package com.server.domain;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
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
@TableName(value = "agriculture_class")
@ApiModel(value = "AgricultureClass", description = "种类信息表")
public class AgricultureClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "class_id")
    @ApiModelProperty(value = "种类ID")
    private Integer classId;

    @TableField(value = "class_name")
    @ApiModelProperty(value = "种类名称")
    private String className;

    @TableField(value = "class_type_name")
    @ApiModelProperty(value = "种类类别名称")
    private String classTypeName;

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
    private Integer orderNum;

    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建者ID")
    private Integer createBy;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_by")
    @ApiModelProperty(value = "修改人ID")
    private Integer updateBy;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

}
