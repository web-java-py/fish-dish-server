package com.server.domain;

import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.server.annotation.Excel;
import com.server.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 分区批次任务对象 agriculture_batch_task
 *
 * @author server
 * @date 2025-05-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="agriculture_batch_task")
@ApiModel(value = "AgricultureBatchTask", description = "分区批次任务表")
public class AgricultureBatchTask extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @TableId(value = "task_id", type = IdType.AUTO)
    @ApiModelProperty(value = "任务ID")
    private Long taskId;

    /** 分区ID */
    @TableField(value = "batch_id")
    @ApiModelProperty(value = "分区ID")
    @Excel(name = "分区ID")
    private Long batchId;

    /** 任务名称 */
    @TableField(value = "task_name")
    @ApiModelProperty(value = "任务名称")
    @Excel(name = "任务名称")
    private String taskName;

    /** 任务负责人ID */
    @TableField(value = "responsible_person_id")
    @ApiModelProperty(value = "任务负责人ID")
    @Excel(name = "任务负责人ID")
    private Long responsiblePersonId;

    /** 任务负责人姓名 */
    @TableField(value = "responsible_person_name")
    @ApiModelProperty(value = "任务负责人姓名")
    @Excel(name = "任务负责人姓名")
    private String responsiblePersonName;

    /** 任务负责人ID */
    @TableField(value = "fish_dish")
    @ApiModelProperty(value = "状态(0是菜,1是鱼)")
    @Excel(name = "状态(0是菜,1是鱼)")
    private Long fishDish;

    /** 计划开始日期 */
    @TableField(value = "plan_start")
    @ApiModelProperty(value = "计划开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planStart;

    /** 计划结束日期 */
    @TableField(value = "plan_finish")
    @ApiModelProperty(value = "计划结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date planFinish;

    /** 实际开始日期 */
    @TableField(value = "actual_start")
    @ApiModelProperty(value = "实际开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualStart;

    /** 实际结束日期 */
    @TableField(value = "actual_finish")
    @ApiModelProperty(value = "实际结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualFinish;

    /** 任务详情 */
    @TableField(value = "task_detail")
    @ApiModelProperty(value = "任务详情")
    @Excel(name = "任务详情")
    private String taskDetail;

    /** 图片资料 */
    @TableField(value = "task_images")
    @ApiModelProperty(value = "图片资料")
    @Excel(name = "图片资料")
    private String taskImages;

    /** 视频资料 */
    @TableField(value = "task_videos")
    @ApiModelProperty(value = "视频资料")
    @Excel(name = "视频资料")
    private String taskVideos;

    /** 任务状态（0未分配 1已分配 2进行中 3已完成） */
    @TableField(value = "status")
    @ApiModelProperty(value = "任务状态（0未分配 1已分配 2进行中 3已完成）")
    @Excel(name = "任务状态", readConverterExp = "0=未分配,1=已分配,2=进行中,3=已完成")
    private String status;

    /** 排序 */
    @TableField(value = "order_num")
    @ApiModelProperty(value = "排序")
    @Excel(name = "排序")
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    @TableField(value = "del_flag")
    @ApiModelProperty(value = "删除标志（0代表存在 2代表删除）")
    private String delFlag;
    /** 搜索值 */
    @TableField(exist = false)
    private String searchValue;

    /** 请求参数 */
    @TableField(exist = false)
    private Map<String, Object> params;

    /** 备注 */
    @TableField(value = "remark")
    @ApiModelProperty(value = "备注")
    private String remark;
}