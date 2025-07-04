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
 * @author bxwy
 * @description  
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="param_type_dict")
@ApiModel(value = "ParamTypeDict" , description="传感器参数类型中英文对照表")
public class ParamTypeDict implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    private Long id;

    @TableField(value="param_type_en")
    @ApiModelProperty(value="参数英文名")
    private String paramTypeEn;

    @TableField(value="param_type_cn")
    @ApiModelProperty(value="参数中文名")
    private String paramTypeCn;

    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;

}
