package com.server.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.server.annotation.Excel;
import com.server.core.domain.BaseEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 雇员对象 agriculture_employee
 * 
 * @author server
 * @date 2025-06-10
 */
@Data
public class AgricultureEmployee implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 雇员ID */
    @TableId(value = "employee_id", type = IdType.AUTO)
    @ApiModelProperty(value = "雇员ID")
    private Long employeeId;

    /** 编码 */
    @TableField(value = "employee_code")
    @ApiModelProperty(value = "编码")
    private String employeeCode;

    /** 姓名 */
    @TableField(value = "employee_name")
    @ApiModelProperty(value = "姓名")
    private String employeeName;

    /** 字典 agriculture_employee_type */
    @TableField(value = "employee_type")
    @Excel(name = "字典 agriculture_employee_type")
    private String employeeType;

    /** 手机号码 */
    @TableField(value = "employee_tel")
    @ApiModelProperty(value = "手机号码")
    private String employeeTel;

    /** 字典 sys_user_sex */
    @TableField(value = "employee_sex")
    @ApiModelProperty(value = "字典 sys_user_sex")
    private String employeeSex;

    /** 地址 */
    @TableField(value = "employee_address")
    @ApiModelProperty(value = "地址")
    private String employeeAddress;

    /** 状态 */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    /** 排序 */
    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    private Long orderNum;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者ID")
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "修改时间")
    private LocalDateTime updateTime;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除） ")
    private String delFlag;



}
