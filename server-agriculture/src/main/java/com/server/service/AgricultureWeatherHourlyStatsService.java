package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureWeatherHourlyStats;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 气象数据小时统计Service接口
 *
 * @author server
 * @date 2025-07-11
 */
public interface AgricultureWeatherHourlyStatsService extends IService<AgricultureWeatherHourlyStats> {

    /**
     * 计算指定时间范围的小时统计数据
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    void calculateHourlyStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 查询指定时间范围的小时统计数据
     *
     * @param pastureId 大棚ID
     * @param batchId 分区ID
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 统计数据列表
     */
    List<AgricultureWeatherHourlyStats> getHourlyStats(String pastureId, String batchId,
                                                       LocalDateTime startTime, LocalDateTime endTime);
}