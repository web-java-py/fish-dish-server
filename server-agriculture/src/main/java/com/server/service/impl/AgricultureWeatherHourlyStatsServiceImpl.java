package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureWeatherData;
import com.server.domain.AgricultureWeatherHourlyStats;
import com.server.mapper.AgricultureWeatherHourlyStatsMapper;
import com.server.mapper.AgricultureWeatherDataMapper;
import com.server.service.AgricultureWeatherHourlyStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

/**
 * 气象数据小时统计Service业务层处理
 *
 * @author server
 * @date 2025-07-11
 */
@Service
public class AgricultureWeatherHourlyStatsServiceImpl extends ServiceImpl<AgricultureWeatherHourlyStatsMapper, AgricultureWeatherHourlyStats>
        implements AgricultureWeatherHourlyStatsService {

    private static final Logger log = LoggerFactory.getLogger(AgricultureWeatherHourlyStatsServiceImpl.class);

    @Autowired
    private AgricultureWeatherDataMapper weatherDataMapper;

    @Override
    public void calculateHourlyStats(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("开始计算小时统计数据，时间范围：{} - {}", startTime, endTime);
        
        // 查询原始数据
        List<AgricultureWeatherData> dataList = weatherDataMapper.selectList(
                new LambdaQueryWrapper<AgricultureWeatherData>()
                        .ge(AgricultureWeatherData::getCollectTime, startTime)
                        .le(AgricultureWeatherData::getCollectTime, endTime)
                        .orderByAsc(AgricultureWeatherData::getCollectTime)
        );

        log.info("查询到 {} 条原始数据", dataList.size());

        if (dataList.isEmpty()) {
            log.warn("没有找到需要统计的数据");
            return;
        }

        // 使用更安全的分组方式
        Map<String, List<AgricultureWeatherData>> groupedData = new HashMap<>();
        
        for (AgricultureWeatherData data : dataList) {
            if (data.getPastureId() == null || data.getBatchId() == null || data.getCollectTime() == null) {
                continue; // 跳过无效数据
            }
            
            LocalDateTime hourTime = data.getCollectTime().withMinute(0).withSecond(0).withNano(0);
            String key = data.getPastureId() + "###" + data.getBatchId() + "###" + hourTime.toString();
            
            groupedData.computeIfAbsent(key, k -> new ArrayList<>()).add(data);
        }

        log.info("分组后共有 {} 个时间段", groupedData.size());

        List<AgricultureWeatherHourlyStats> statsList = new ArrayList<>();
        for (Map.Entry<String, List<AgricultureWeatherData>> entry : groupedData.entrySet()) {
            try {
                String[] parts = entry.getKey().split("###");
                
                if (parts.length != 3) {
                    log.warn("分组键格式不正确：{}", entry.getKey());
                    continue;
                }
                
                String pastureId = parts[0];
                String batchId = parts[1];
                LocalDateTime statHour = LocalDateTime.parse(parts[2]);
                List<AgricultureWeatherData> groupData = entry.getValue();

                log.debug("处理分组：pastureId={}, batchId={}, hour={}, 数据条数={}", 
                         pastureId, batchId, statHour, groupData.size());

                // 计算平均值
                AgricultureWeatherHourlyStats stats = AgricultureWeatherHourlyStats.builder()
                                .pastureId(pastureId)
                                .batchId(batchId)
                                .statHour(statHour)
                                .avgTemperature(nullToZero(calculateAverage(groupData, AgricultureWeatherData::getTemperature)))
                                .avgHumidity(nullToZero(calculateAverage(groupData, AgricultureWeatherData::getHumidity)))
                                .avgWindSpeed(nullToZero(calculateAverage(groupData, AgricultureWeatherData::getWindSpeed)))
                                .avgLightIntensity(nullToZero(calculateAverage(groupData, AgricultureWeatherData::getLightIntensity)))
                                .avgRainfall(nullToZero(calculateAverage(groupData, AgricultureWeatherData::getRainfall)))
                                .avgAirPressure(nullToZero(calculateAverage(groupData, AgricultureWeatherData::getAirPressure)))
                                .dataCount(groupData.size())
                                .createTime(LocalDateTime.now())
                                .build();

                statsList.add(stats);
                
            } catch (Exception e) {
                log.error("处理分组数据时发生错误，分组键：{}，错误：{}", entry.getKey(), e.getMessage(), e);
            }
        }

        log.info("生成了 {} 条统计数据", statsList.size());

        // 批量保存或更新
        if (!statsList.isEmpty()) {
            int savedCount = 0;
            for (AgricultureWeatherHourlyStats stats : statsList) {
                try {
                    // 检查是否已存在
                    AgricultureWeatherHourlyStats existing = this.getOne(
                            new LambdaQueryWrapper<AgricultureWeatherHourlyStats>()
                                    .eq(AgricultureWeatherHourlyStats::getPastureId, stats.getPastureId())
                                    .eq(AgricultureWeatherHourlyStats::getBatchId, stats.getBatchId())
                                    .eq(AgricultureWeatherHourlyStats::getStatHour, stats.getStatHour())
                    );

                    if (existing != null) {
                        stats.setId(existing.getId());
                        this.updateById(stats);
                        log.debug("更新统计数据：{}", stats.getStatHour());
                    } else {
                        this.save(stats);
                        log.debug("保存统计数据：{}", stats.getStatHour());
                    }
                    savedCount++;
                    
                } catch (Exception e) {
                    log.error("保存统计数据时发生错误：{}", e.getMessage());
                }
            }
            log.info("成功保存 {} 条统计数据", savedCount);
        }
    }

    @Override
    public List<AgricultureWeatherHourlyStats> getHourlyStats(String pastureId, String batchId,
                                                              LocalDateTime startTime, LocalDateTime endTime) {
        return this.list(new LambdaQueryWrapper<AgricultureWeatherHourlyStats>()
                .eq(AgricultureWeatherHourlyStats::getPastureId, pastureId)
                .eq(AgricultureWeatherHourlyStats::getBatchId, batchId)
                .ge(AgricultureWeatherHourlyStats::getStatHour, startTime)
                .le(AgricultureWeatherHourlyStats::getStatHour, endTime)
                .orderByAsc(AgricultureWeatherHourlyStats::getStatHour));
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

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP) : value;
    }
}