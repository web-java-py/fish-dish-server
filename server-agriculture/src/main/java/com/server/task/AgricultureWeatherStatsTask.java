package com.server.task;

import com.server.service.AgricultureWeatherDailyStatsService;
import com.server.service.AgricultureWeatherHourlyStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 农业气象数据统计任务
 *
 * @author server
 */
@Component
public class AgricultureWeatherStatsTask {

    @Autowired
    private AgricultureWeatherDailyStatsService dailyStatsService;

    @Autowired
    private AgricultureWeatherHourlyStatsService hourlyStatsService;

    /**
     * 计算每日统计数据（每天凌晨1点执行）
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void calculateDailyStats() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime startTime = yesterday.atStartOfDay();
        LocalDateTime endTime = yesterday.atTime(LocalTime.MAX);

        dailyStatsService.calculateDailyStats(startTime, endTime);
    }

    /**
     * 计算每小时统计数据（每小时执行一次）
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void calculateHourlyStats() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusHours(1).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endTime = startTime.plusHours(1);

        hourlyStatsService.calculateHourlyStats(startTime, endTime);
    }
}