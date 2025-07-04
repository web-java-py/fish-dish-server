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
import java.util.Date;

/**
 * 批次任务日志对象 agriculture_task_log
 * 
 * @author server
 * @date 2025-06-11
 */
@Data
public class AgricultureTaskLog implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 日志ID */
    @TableId(value = "log_id", type = IdType.AUTO)
    @ApiModelProperty(value = "日志ID")
    private Long logId;

    /** 任务ID */
    @TableField(value = "task_id")
    @ApiModelProperty(value = "任务ID")
    private Long taskId;

    /** 操作人名称 */
    @TableField(value = "oper_name")
    @ApiModelProperty(value = "操作人名称")
    private String operName;

    /** 操作人Id */
    @TableField(value = "oper_id")
    @ApiModelProperty(value = "操作人Id")
    private Long operId;

    /** 操作描述 */
    @TableField(value = "oper_des")
    @ApiModelProperty(value = "操作描述")
    private String operDes;

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
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除")
    private String delFlag;


}
