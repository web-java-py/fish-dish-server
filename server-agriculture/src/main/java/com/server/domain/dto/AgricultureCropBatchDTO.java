package com.server.domain.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Author: zbb
 * @Date: 2025/5/28 15:40
 */
@Data
public class AgricultureCropBatchDTO {
    private String batchName;
    // 从 agriculture_crop_batch 表获取
    @TableId(value="batch_id",type = IdType.AUTO)
    private String batchId;
    private Long germplasmId;
    private Long pastureId;            // 从 agriculture_crop_batch 表获取
    private LocalDateTime startTime;
    private double cropArea;       // 从 agriculture_crop_batch 表获取
    private String contractAddr;       // 从 agriculture_crop_batch 表获取
    private LocalDate createTime;           // 从 agriculture_crop_batch 表获取
    private String classImage;         // 从 agriculture_class 表获取
    private String className;
}
