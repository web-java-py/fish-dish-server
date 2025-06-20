package com.server.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

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

/**
 * 饵料投喂对象 fish_cost_bait
 * 
 * @author server
 * @date 2025-06-14
 */
@Data
public class AgricultureCostBait implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @TableId(value = "cost_id", type = IdType.AUTO)
    @ApiModelProperty(value = "costId")
    private String costId;

    /** 任务ID */
    @TableField(value = "task_id")
    @ApiModelProperty(value = "任务ID")
    private Long taskId;

    /** 饵料ID */
    @TableField(value = "bait_id")
    @ApiModelProperty(value = "饵料ID")
    private Long baitId;

    /** 饵料数量 */
    @TableField(value = "bait_count")
    @ApiModelProperty(value = "饵料数量")
    private Long baitCount;

    /** 计量单位 */
    @TableField(value = "measure_unit")
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workingStart;

    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date workingFinish;

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
}
