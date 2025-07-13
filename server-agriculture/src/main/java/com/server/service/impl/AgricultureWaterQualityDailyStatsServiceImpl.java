package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureWaterQualityData;
import com.server.domain.AgricultureWaterQualityDailyStats;
import com.server.mapper.AgricultureWaterQualityDailyStatsMapper;
import com.server.mapper.AgricultureWaterQualityDataMapper;
import com.server.service.AgricultureWaterQualityDailyStatsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 水质数据日统计Service业务层处理
 *
 * @author server
 * @date 2025-07-11
 */
@Service
public class AgricultureWaterQualityDailyStatsServiceImpl extends ServiceImpl<AgricultureWaterQualityDailyStatsMapper, AgricultureWaterQualityDailyStats>
        implements AgricultureWaterQualityDailyStatsService {

    private static final Logger log = LoggerFactory.getLogger(AgricultureWaterQualityDailyStatsServiceImpl.class);

    @Autowired
    private AgricultureWaterQualityDataMapper waterQualityDataMapper;

    @Override
    public void calculateDailyStats(LocalDateTime startTime, LocalDateTime endTime) {
        log.info("开始计算水质日统计数据，时间范围：{} - {}", startTime, endTime);
        
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

        // 按大棚、分区、日期分组
        Map<String, List<AgricultureWaterQualityData>> groupedData = dataList.stream()
                .filter(data -> data.getPastureId() != null && data.getBatchId() != null && data.getCollectTime() != null)
                .collect(Collectors.groupingBy(data ->
                        data.getPastureId() + "_" + data.getBatchId() + "_" +
                                data.getCollectTime().toLocalDate()));

        log.info("分组后共有 {} 个日期", groupedData.size());

        List<AgricultureWaterQualityDailyStats> statsList = new ArrayList<>();
        for (Map.Entry<String, List<AgricultureWaterQualityData>> entry : groupedData.entrySet()) {
            try {
                String[] keys = entry.getKey().split("_");
                if (keys.length != 3) {
                    log.warn("分组键格式不正确：{}", entry.getKey());
                    continue;
                }
                
                String pastureId = keys[0];
                String batchId = keys[1];
                LocalDate statDate = LocalDate.parse(keys[2]);
                List<AgricultureWaterQualityData> groupData = entry.getValue();

                log.debug("处理分组：pastureId={}, batchId={}, date={}, 数据条数={}", 
                         pastureId, batchId, statDate, groupData.size());

                // 计算平均值
                AgricultureWaterQualityDailyStats stats = AgricultureWaterQualityDailyStats.builder()
                        .pastureId(pastureId)
                        .batchId(batchId)
                        .statDate(statDate)
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
            for (AgricultureWaterQualityDailyStats stats : statsList) {
                try {
                    // 检查是否已存在
                    AgricultureWaterQualityDailyStats existing = this.getOne(
                            new LambdaQueryWrapper<AgricultureWaterQualityDailyStats>()
                                    .eq(AgricultureWaterQualityDailyStats::getPastureId, stats.getPastureId())
                                    .eq(AgricultureWaterQualityDailyStats::getBatchId, stats.getBatchId())
                                    .eq(AgricultureWaterQualityDailyStats::getStatDate, stats.getStatDate())
                    );

                    if (existing != null) {
                        stats.setId(existing.getId());
                        this.updateById(stats);
                        log.debug("更新统计数据：{}", stats.getStatDate());
                    } else {
                        this.save(stats);
                        log.debug("保存统计数据：{}", stats.getStatDate());
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
    public List<AgricultureWaterQualityDailyStats> getDailyStats(String pastureId, String batchId,
                                                                 LocalDateTime startDate, LocalDateTime endDate) {
        return this.list(new LambdaQueryWrapper<AgricultureWaterQualityDailyStats>()
                .eq(AgricultureWaterQualityDailyStats::getPastureId, pastureId)
                .eq(AgricultureWaterQualityDailyStats::getBatchId, batchId)
                .ge(AgricultureWaterQualityDailyStats::getStatDate, startDate.toLocalDate())
                .le(AgricultureWaterQualityDailyStats::getStatDate, endDate.toLocalDate())
                .orderByAsc(AgricultureWaterQualityDailyStats::getStatDate));
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