package com.server.task;

import com.server.service.AgricultureWeatherDailyStatsService;
import com.server.service.AgricultureWeatherHourlyStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 农业最近10天数据导入任务
 *
 * @author server
 */
@Component
public class AgricultureRecent10DaysImportTask {

    private static final Logger log = LoggerFactory.getLogger(AgricultureRecent10DaysImportTask.class);

    @Autowired
    private AgricultureWeatherDailyStatsService dailyStatsService;

    @Autowired
    private AgricultureWeatherHourlyStatsService hourlyStatsService;

    /**
     * 导入最近10天的日统计和小时统计数据
     */
    public void importRecent10DaysData() {
        log.info("开始导入最近10天的统计数据...");

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(10);

        log.info("导入时间范围：{} - {}", startTime, endTime);

        // 导入日统计数据
        importDailyStatsFor10Days(startTime, endTime);

        // 导入小时统计数据
        importHourlyStatsFor10Days(startTime, endTime);

        log.info("最近10天数据导入完成！");
    }

    /**
     * 导入最近10天的日统计数据
     */
    private void importDailyStatsFor10Days(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("开始导入最近10天的日统计数据...");

        LocalDate startDate = startTime.toLocalDate();
        LocalDate endDate = endTime.toLocalDate();

        LocalDate currentDate = startDate;
        int processedDays = 0;

        while (!currentDate.isAfter(endDate)) {
            try {
                LocalDateTime dayStart = currentDate.atStartOfDay();
                LocalDateTime dayEnd = currentDate.atTime(LocalTime.MAX);

                log.info("处理日统计日期：{}", currentDate);
                dailyStatsService.calculateDailyStats(dayStart, dayEnd);

                processedDays++;
                currentDate = currentDate.plusDays(1);

            } catch (Exception e) {
                log.error("处理日统计日期 {} 时发生错误：{}", currentDate, e.getMessage());
                currentDate = currentDate.plusDays(1);
            }
        }

        log.info("日统计数据导入完成，共处理 {} 天的数据", processedDays);
    }

    /**
     * 导入最近10天的小时统计数据
     */
    private void importHourlyStatsFor10Days(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("开始导入最近10天的小时统计数据...");

        LocalDateTime currentDateTime = startTime.withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDateTime = endTime.withMinute(0).withSecond(0).withNano(0);

        int processedHours = 0;

        while (!currentDateTime.isAfter(endDateTime)) {
            try {
                LocalDateTime hourStart = currentDateTime;
                LocalDateTime hourEnd = currentDateTime.plusHours(1);

                log.info("处理小时统计时间：{}", currentDateTime);
                hourlyStatsService.calculateHourlyStats(hourStart, hourEnd);

                processedHours++;
                currentDateTime = currentDateTime.plusHours(1);

                // 每处理24小时输出一次进度
                if (processedHours % 24 == 0) {
                    log.info("已处理 {} 小时的数据", processedHours);
                }

            } catch (Exception e) {
                log.error("处理小时统计时间 {} 时发生错误：{}", currentDateTime, e.getMessage());
                currentDateTime = currentDateTime.plusHours(1);
            }
        }

        log.info("小时统计数据导入完成，共处理 {} 小时的数据", processedHours);
    }

    /**
     * 只导入日统计数据
     */
    public void importDailyStatsOnly() {
        log.info("开始导入最近10天的日统计数据...");

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(10);

        importDailyStatsFor10Days(startTime, endTime);
    }

    /**
     * 只导入小时统计数据
     */
    public void importHourlyStatsOnly() {
        log.info("开始导入最近10天的小时统计数据...");

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(10);

        importHourlyStatsFor10Days(startTime, endTime);
    }
}