package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureWeatherData;
import com.server.domain.AgricultureWeatherDailyStats;
import com.server.mapper.AgricultureWeatherDailyStatsMapper;
import com.server.mapper.AgricultureWeatherDataMapper;
import com.server.service.AgricultureWeatherDailyStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 气象数据日统计Service业务层处理
 *
 * @author server
 * @date 2025-07-11
 */
@Service
public class AgricultureWeatherDailyStatsServiceImpl extends ServiceImpl<AgricultureWeatherDailyStatsMapper, AgricultureWeatherDailyStats>
        implements AgricultureWeatherDailyStatsService {

    @Autowired
    private AgricultureWeatherDataMapper weatherDataMapper;

    @Override
    public void calculateDailyStats(LocalDateTime startTime, LocalDateTime endTime) {
        // 查询原始数据
        List<AgricultureWeatherData> dataList = weatherDataMapper.selectList(
                new LambdaQueryWrapper<AgricultureWeatherData>()
                        .ge(AgricultureWeatherData::getCollectTime, startTime)
                        .le(AgricultureWeatherData::getCollectTime, endTime)
                        .orderByAsc(AgricultureWeatherData::getCollectTime)
        );

        // 按大棚、分区、日期分组
        Map<String, List<AgricultureWeatherData>> groupedData = dataList.stream()
                .collect(Collectors.groupingBy(data ->
                        data.getPastureId() + "_" + data.getBatchId() + "_" +
                                data.getCollectTime().toLocalDate()));

        List<AgricultureWeatherDailyStats> statsList = new ArrayList<>();
        for (Map.Entry<String, List<AgricultureWeatherData>> entry : groupedData.entrySet()) {
            String[] keys = entry.getKey().split("_");
            String pastureId = keys[0];
            String batchId = keys[1];
            LocalDate statDate = LocalDate.parse(keys[2]);
            List<AgricultureWeatherData> groupData = entry.getValue();

            // 计算平均值
            AgricultureWeatherDailyStats stats = AgricultureWeatherDailyStats.builder()
                    .pastureId(pastureId)
                    .batchId(batchId)
                    .statDate(statDate)
                    .avgTemperature(calculateAverage(groupData, AgricultureWeatherData::getTemperature))
                    .avgHumidity(calculateAverage(groupData, AgricultureWeatherData::getHumidity))
                    .avgWindSpeed(calculateAverage(groupData, AgricultureWeatherData::getWindSpeed))
                    .avgLightIntensity(calculateAverage(groupData, AgricultureWeatherData::getLightIntensity))
                    .avgRainfall(calculateAverage(groupData, AgricultureWeatherData::getRainfall))
                    .avgAirPressure(calculateAverage(groupData, AgricultureWeatherData::getAirPressure))
                    .dataCount(groupData.size())
                    .createTime(LocalDateTime.now())
                    .build();

            statsList.add(stats);
        }

        // 批量保存或更新
        if (!statsList.isEmpty()) {
            for (AgricultureWeatherDailyStats stats : statsList) {
                // 检查是否已存在
                AgricultureWeatherDailyStats existing = this.getOne(
                        new LambdaQueryWrapper<AgricultureWeatherDailyStats>()
                                .eq(AgricultureWeatherDailyStats::getPastureId, stats.getPastureId())
                                .eq(AgricultureWeatherDailyStats::getBatchId, stats.getBatchId())
                                .eq(AgricultureWeatherDailyStats::getStatDate, stats.getStatDate())
                );

                if (existing != null) {
                    stats.setId(existing.getId());
                    this.updateById(stats);
                } else {
                    this.save(stats);
                }
            }
        }
    }

    @Override
    public List<AgricultureWeatherDailyStats> getDailyStats(String pastureId, String batchId,
                                                            LocalDateTime startDate, LocalDateTime endDate) {
        return this.list(new LambdaQueryWrapper<AgricultureWeatherDailyStats>()
                .eq(AgricultureWeatherDailyStats::getPastureId, pastureId)
                .eq(AgricultureWeatherDailyStats::getBatchId, batchId)
                .ge(AgricultureWeatherDailyStats::getStatDate, startDate.toLocalDate())
                .le(AgricultureWeatherDailyStats::getStatDate, endDate.toLocalDate())
                .orderByAsc(AgricultureWeatherDailyStats::getStatDate));
    }

    /**
     * 计算平均值
     */
    private BigDecimal calculateAverage(List<AgricultureWeatherData> dataList,
                                        java.util.function.Function<AgricultureWeatherData, Double> getter) {
        List<Double> validValues = dataList.stream()
                .map(getter)
                .filter(value -> value != null && value != 0)
                .collect(Collectors.toList());

        if (validValues.isEmpty()) {
            return null;
        }

        double avg = validValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return new BigDecimal(avg).setScale(2, RoundingMode.HALF_UP);
    }
}