package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureWaterQualityData;
import com.server.domain.AgricultureWaterQualityHourlyStats;
import com.server.mapper.AgricultureWaterQualityHourlyStatsMapper;
import com.server.mapper.AgricultureWaterQualityDataMapper;
import com.server.service.AgricultureWaterQualityHourlyStatsService;
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
 * 水质数据小时统计Service业务层处理
 *
 * @author server
 * @date 2025-07-11
 */
@Service
public class AgricultureWaterQualityHourlyStatsServiceImpl extends ServiceImpl<AgricultureWaterQualityHourlyStatsMapper, AgricultureWaterQualityHourlyStats>
        implements AgricultureWaterQualityHourlyStatsService {

    private static final Logger log = LoggerFactory.getLogger(AgricultureWaterQualityHourlyStatsServiceImpl.class);

    @Autowired
    private AgricultureWaterQualityDataMapper waterQualityDataMapper;

    @Override
    public void calculateHourlyStats(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("开始计算水质小时统计数据，时间范围：{} - {}", startTime, endTime);
        
        // 查询原始数据
        List<AgricultureWaterQualityData> dataList = waterQualityDataMapper.selectList(
                new LambdaQueryWrapper<AgricultureWaterQualityData>()
                        .ge(AgricultureWaterQualityData::getCollectTime, startTime)
                        .le(AgricultureWaterQualityData::getCollectTime, endTime)
                        .orderByAsc(AgricultureWaterQualityData::getCollectTime)
        );

        log.info("查询到 {} 条原始数据", dataList.size());

        if (dataList.isEmpty()) {
            log.warn("没有找到需要统计的数据");
            return;
        }

        // 使用更安全的分组方式
        Map<String, List<AgricultureWaterQualityData>> groupedData = new HashMap<>();
        
        for (AgricultureWaterQualityData data : dataList) {
            if (data.getPastureId() == null || data.getBatchId() == null || data.getCollectTime() == null) {
                continue; // 跳过无效数据
            }
            
            LocalDateTime hourTime = data.getCollectTime().withMinute(0).withSecond(0).withNano(0);
            String key = data.getPastureId() + "###" + data.getBatchId() + "###" + hourTime.toString();
            
            groupedData.computeIfAbsent(key, k -> new ArrayList<>()).add(data);
        }

        log.info("分组后共有 {} 个时间段", groupedData.size());

        List<AgricultureWaterQualityHourlyStats> statsList = new ArrayList<>();
        for (Map.Entry<String, List<AgricultureWaterQualityData>> entry : groupedData.entrySet()) {
            try {
                String[] parts = entry.getKey().split("###");
                
                if (parts.length != 3) {
                    log.warn("分组键格式不正确：{}", entry.getKey());
                    continue;
                }
                
                String pastureId = parts[0];
                String batchId = parts[1];
                LocalDateTime statHour = LocalDateTime.parse(parts[2]);
                List<AgricultureWaterQualityData> groupData = entry.getValue();

                log.debug("处理分组：pastureId={}, batchId={}, hour={}, 数据条数={}", 
                         pastureId, batchId, statHour, groupData.size());

                // 计算平均值
                AgricultureWaterQualityHourlyStats stats = AgricultureWaterQualityHourlyStats.builder()
                                .pastureId(pastureId)
                                .batchId(batchId)
                                .statHour(statHour)
                                .avgPhValue(nullToZero(calculateAverage(groupData, AgricultureWaterQualityData::getPhValue)))
                                .avgDissolvedOxygen(nullToZero(calculateAverage(groupData, AgricultureWaterQualityData::getDissolvedOxygen)))
                                .avgAmmoniaNitrogen(nullToZero(calculateAverage(groupData, AgricultureWaterQualityData::getAmmoniaNitrogen)))
                                .avgWaterTemperature(nullToZero(calculateAverage(groupData, AgricultureWaterQualityData::getWaterTemperature)))
                                .avgConductivity(nullToZero(calculateAverage(groupData, AgricultureWaterQualityData::getConductivity)))
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
            for (AgricultureWaterQualityHourlyStats stats : statsList) {
                try {
                    // 检查是否已存在
                    AgricultureWaterQualityHourlyStats existing = this.getOne(
                            new LambdaQueryWrapper<AgricultureWaterQualityHourlyStats>()
                                    .eq(AgricultureWaterQualityHourlyStats::getPastureId, stats.getPastureId())
                                    .eq(AgricultureWaterQualityHourlyStats::getBatchId, stats.getBatchId())
                                    .eq(AgricultureWaterQualityHourlyStats::getStatHour, stats.getStatHour())
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
    public List<AgricultureWaterQualityHourlyStats> getHourlyStats(String pastureId, String batchId,
                                                                   LocalDateTime startTime, LocalDateTime endTime) {
        return this.list(new LambdaQueryWrapper<AgricultureWaterQualityHourlyStats>()
                .eq(AgricultureWaterQualityHourlyStats::getPastureId, pastureId)
                .eq(AgricultureWaterQualityHourlyStats::getBatchId, batchId)
                .ge(AgricultureWaterQualityHourlyStats::getStatHour, startTime)
                .le(AgricultureWaterQualityHourlyStats::getStatHour, endTime)
                .orderByAsc(AgricultureWaterQualityHourlyStats::getStatHour));
    }

    /**
     * 计算平均值
     */
    private BigDecimal calculateAverage(List<AgricultureWaterQualityData> dataList, 
                                       java.util.function.Function<AgricultureWaterQualityData, Double> getter) {
        List<Double> values = dataList.stream()
                .map(getter)
                .filter(val -> val != null)
                .collect(Collectors.toList());
        
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        double sum = values.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / values.size();
        return BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 将null转换为0
     */
    private BigDecimal nullToZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }
} 