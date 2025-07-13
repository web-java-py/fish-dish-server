package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureWaterQualityDailyStats;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 水质数据日统计Service接口
 *
 * @author server
 * @date 2025-07-11
 */
public interface AgricultureWaterQualityDailyStatsService extends IService<AgricultureWaterQualityDailyStats> {

    /**
     * 计算指定时间范围的日统计数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void calculateDailyStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询指定时间范围的日统计数据
     *
     * @param pastureId 大棚ID
     * @param batchId 分区ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计数据列表
     */
    List<AgricultureWaterQualityDailyStats> getDailyStats(String pastureId, String batchId,
                                                          LocalDateTime startDate, LocalDateTime endDate);
} 