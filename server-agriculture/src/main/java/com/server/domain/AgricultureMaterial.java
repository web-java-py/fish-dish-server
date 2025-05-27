package com.server.domain;

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
@TableName(value ="agriculture_material")
@ApiModel(value = "AgricultureMaterial" , description="农资信息表")
public class AgricultureMaterial extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="material_id")
    @ApiModelProperty(value="农资ID")
    private Long materialId;

    @TableField(value="material_code")
    @ApiModelProperty(value="农资编码")
    private String materialCode;

    @TableField(value="material_name")
    @ApiModelProperty(value="农资名称")
    private String materialName;

    @TableField(value="material_image")
    @ApiModelProperty(value="农资图片")
    private String materialImage;

    @TableField(value="measure_unit")
    @ApiModelProperty(value="计量单位")
    private String measureUnit;

    @TableField(value="status")
    @ApiModelProperty(value="状态")
    private String status;

    @TableField(value="order_num")
    @ApiModelProperty(value="排序")
    private Long orderNum;

    @TableField(value="del_flag")
    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;

}
