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
 * @author 851543
 * @description
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "agriculture_job")
@ApiModel(value = "AgricultureJob", description = "作业任务表")
public class AgricultureJob extends BaseEntityPlus implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "job_id", type = IdType.AUTO)
    @ApiModelProperty(value = "作业任务ID")
    private Long jobId;

    @TableField(value = "class_id")
    @ApiModelProperty(value = "种类ID")
    private Long classId;

    @TableField(value = "job_name")
    @ApiModelProperty(value = "作业任务名称")
    private String jobName;

    @TableField(value = "cycle_unit")
    @ApiModelProperty(value = "作业周期单位（0代表周 1代表天）")
    private String cycleUnit;

    @TableField(value = "job_start")
    @ApiModelProperty(value = "起始周/天")
    private Long jobStart;

    @TableField(value = "job_finish")
    @ApiModelProperty(value = "结束周/天")
    private Long jobFinish;

    @TableField(value = "status")
    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

}
