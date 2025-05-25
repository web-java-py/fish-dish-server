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
@TableName(value ="agriculture_material_info")
@ApiModel(value = "AgricultureMaterialInfo" , description="农资信息表")
public class AgricultureMaterialInfo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value="material_id")
    @ApiModelProperty(value="农资ID")
    private Integer materialId;

    @TableField(value="material_code")
    @ApiModelProperty(value="农资编码")
    private String materialCode;

    @TableField(value="material_name")
    @ApiModelProperty(value="农资名称")
    private String materialName;

    @TableField(value="material_type_id")
    @ApiModelProperty(value="农资类别")
    private Integer materialTypeId;

    @TableField(value="measure_unit")
    @ApiModelProperty(value="计量单位")
    private String measureUnit;

    @TableField(value="remark")
    @ApiModelProperty(value="备注")
    private String remark;

    @TableField(value="status")
    @ApiModelProperty(value="状态")
    private String status;

    @TableField(value="order_num")
    @ApiModelProperty(value="排序")
    private Integer orderNum;

    @TableField(value="create_by")
    @ApiModelProperty(value="创建者ID")
    private Integer createBy;

    @TableField(value="create_time")
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    @TableField(value="update_by")
    @ApiModelProperty(value="修改人ID")
    private Integer updateBy;

    @TableField(value="update_time")
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;

    @TableField(value="del_flag")
    @ApiModelProperty(value="删除标志（0代表存在 2代表删除）")
    private String delFlag;

}
