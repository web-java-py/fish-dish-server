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
@TableName(value ="agriculture_device_type")
@ApiModel(value = "AgricultureDeviceType" , description="设备类型表")
public class AgricultureDeviceType extends BaseEntityPlus implements Serializable  {

    private static final long serialVersionUID = 1L;

    @TableId(value="id",type = IdType.AUTO)
    @ApiModelProperty(value="主键ID")
    private Long id;

    @TableField(value="type_code")
    @ApiModelProperty(value="类型编码")
    private String typeCode;

    @TableField(value="type_name")
    @ApiModelProperty(value="类型名称")
    private String typeName;

    @TableField(value="type_desc")
    @ApiModelProperty(value="类型描述")
    private String typeDesc;

    @TableField(value="is_controllable")
    @ApiModelProperty(value="是否可控设备")
    private String isControllable;

}
