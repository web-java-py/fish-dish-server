package com.server.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import com.server.annotation.Excel;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 饵料信息对象 agriculture_bait_info
 * 
 * @author server
 * @date 2025-06-14
 */
@Data
public class AgricultureBaitInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 饵料ID */
    @TableId(value = "bait_id", type = IdType.AUTO)
    @ApiModelProperty(value = "饵料ID")
    private String baitId;

    /** 饵料编码 */
    @TableField(value = "bait_code")
    @ApiModelProperty(value = "饵料编码")
    private String baitCode;

    /** 饵料名称 */
    @TableField(value = "bait_name")
    @ApiModelProperty(value = "饵料名称")
    private String baitName;

    /** 投喂数量 */
    @TableField(value = "bait_sl")
    @ApiModelProperty(value = "投喂数量")
    private Long baitSl;

    /** 计量单位 */
    @TableField(value = "measure_unit")
    @ApiModelProperty(value = "计量单位")
    private String measureUnit;

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
