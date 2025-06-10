package com.server.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.server.annotation.Excel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Component
public class AgricultureBatchTaskDTO{

    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @TableId(value = "taskId", type = IdType.AUTO)
    private Long taskId;

    /** 批次ID */
    private Long batchId;


    /** 任务名称 */
    private String taskName;

    /** 计划开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStart;

    /** 计划结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planFinish;

    /** 实际开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualStart;

    /** 实际结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualFinish;

    /** 任务详情 */
    private String taskDetail;

    /** 图片资料 */
    private String taskImages;

    /** 视频资料 */
    private String taskVideos;

    /** 状态 */
    private String status;

    /** 排序 */
    private Long orderNum;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    private Long batchHead;

    private String userName;

    private  Long landId;

    private String landName;

    private  String batchName;

    private  Long taskHead;

    private  String taskHeadName;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;



}
