package com.server.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @Author: zbb
 * @Date: 2025/5/23 16:30
 */
/**
 * 农资信息表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_material")
@ApiModel(value = "AgricultureMaterial",description = "农资信息表")
public class AgricultureMaterial implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField(value = "material_id")
    @ApiModelProperty(value = "农资ID")
    private Long materialId;

    @TableField(value = "material_code")
    @ApiModelProperty(value = "农资编码")
    private String materialCode;

    @TableField(value = "material_name")
    @ApiModelProperty(value = "农资名称")
    private String materialName;

    @TableField(value = "material_type_id")
    @ApiModelProperty(value = "农资类别")
    private Long materialTypeId;

    @TableField(value = "measure_unit")
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    private Long orderNum;

    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建者ID")
    private String createBy;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_by")
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;
}
