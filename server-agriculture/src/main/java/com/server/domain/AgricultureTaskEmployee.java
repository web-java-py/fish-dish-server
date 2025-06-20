package com.server.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.server.annotation.Excel;
import com.server.core.domain.BaseEntity;

import java.io.Serializable;

/**
 * 批次任务工人对象 agriculture_task_employee
 * 
 * @author server
 * @date 2025-06-10
 */
@Data
public class AgricultureTaskEmployee implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private String id;

    /** 任务ID */
    @TableField(value = "task_id")
    @ApiModelProperty(value = "任务ID")
    private Long taskId;

    /** 员工ID */
    @TableField(value = "employee_id")
    @ApiModelProperty(value = "员工ID")
    private Long employeeId;

    /** 状态 */
    @TableField(value = "status")
    @ApiModelProperty(value = "状态")
    private String status;

    /** 排序 */
    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;

}
