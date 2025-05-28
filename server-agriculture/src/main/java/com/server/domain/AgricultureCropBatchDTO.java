package com.server.domain;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author: zbb
 * @Date: 2025/5/28 15:40
 */
@Data
public class AgricultureCropBatchDTO {
    private String batchName;          // 从 agriculture_crop_batch 表获取
    private Long pastureId;            // 从 agriculture_crop_batch 表获取
    private double cropArea;       // 从 agriculture_crop_batch 表获取
    private String contractAddr;       // 从 agriculture_crop_batch 表获取
    private LocalDateTime createTime;           // 从 agriculture_crop_batch 表获取
    private String classImage;         // 从 agriculture_class 表获取
    private String className;
}
