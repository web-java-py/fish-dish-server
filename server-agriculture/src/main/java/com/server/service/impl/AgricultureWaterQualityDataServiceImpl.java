package com.server.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureWaterQualityData;
import com.server.domain.AgricultureWeatherData;
import com.server.domain.AgricultureWaterQualityDailyStats;
import com.server.domain.AgricultureWaterQualityHourlyStats;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.service.AgricultureWaterQualityDailyStatsService;
import com.server.service.AgricultureWaterQualityHourlyStatsService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureWaterQualityDataMapper;
import com.server.service.AgricultureWaterQualityDataService;

import static com.server.utils.Arith.round;

/**
 * 水质数据Service业务层处理
 * 
 * @author server
 * @date 2025-06-08
 */
@Service
public class AgricultureWaterQualityDataServiceImpl extends ServiceImpl<AgricultureWaterQualityDataMapper, AgricultureWaterQualityData> implements AgricultureWaterQualityDataService
{
    @Autowired
    private AgricultureWaterQualityDataMapper agricultureWaterQualityDataMapper;

    @Autowired
    private AgricultureWaterQualityDailyStatsService dailyStatsService;

    @Autowired
    private AgricultureWaterQualityHourlyStatsService hourlyStatsService;


    /**
     * 查询水质数据
     * 
     * @param id 水质数据主键
     * @return 水质数据
     */
    @Override
    public AgricultureWaterQualityData selectAgricultureWaterQualityDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询水质数据列表
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 水质数据
     */
    @Override
    public List<AgricultureWaterQualityData> selectAgricultureWaterQualityDataList(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return list();
    }

    //查询水质趋势数据图表数据
    @Override
    public Map<String, Object> getTrendData(String pastureId, String batchId, String range) {
        // 优先查询统计表，如果没有数据再查询原始表
        if ("day".equals(range)) {
            return getHourlyTrendData(pastureId, batchId);
        } else {
            return getDailyTrendData(pastureId, batchId, range);
        }
    }

    /**
     * 获取小时趋势数据（优先查询统计表）
     */
    private Map<String, Object> getHourlyTrendData(String pastureId, String batchId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
        
        // 先查询统计表
        List<AgricultureWaterQualityHourlyStats> stats = hourlyStatsService.getHourlyStats(pastureId, batchId, start, now);
        
        if (!stats.isEmpty()) {
            return buildTrendDataFromHourlyStats(stats);
        }
        
        // 统计表没有数据，查询原始表
        return getTrendDataFromRawData(pastureId, batchId, "day");
    }

    /**
     * 获取日趋势数据（优先查询统计表）
     */
    private Map<String, Object> getDailyTrendData(String pastureId, String batchId, String range) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        if ("week".equals(range)) {
            start = now.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
        } else { // month
            start = now.minusDays(29).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        
        // 先查询统计表
        List<AgricultureWaterQualityDailyStats> stats = dailyStatsService.getDailyStats(pastureId, batchId, start, now);
        
        if (!stats.isEmpty()) {
            return buildTrendDataFromDailyStats(stats, range);
        }
        
        // 统计表没有数据，查询原始表
        return getTrendDataFromRawData(pastureId, batchId, range);
    }

    /**
     * 从小时统计数据构建趋势数据
     */
    private Map<String, Object> buildTrendDataFromHourlyStats(List<AgricultureWaterQualityHourlyStats> stats) {
        List<String> xAxis = new ArrayList<>();
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> ammoniaNitrogen = new ArrayList<>();
        List<Double> waterTemperature = new ArrayList<>();
        List<Double> phValue = new ArrayList<>();
        List<Double> conductivity = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
        
        for (AgricultureWaterQualityHourlyStats stat : stats) {
            xAxis.add(stat.getStatHour().format(formatter));
            dissolvedOxygen.add(stat.getAvgDissolvedOxygen() != null ? stat.getAvgDissolvedOxygen().doubleValue() : null);
            ammoniaNitrogen.add(stat.getAvgAmmoniaNitrogen() != null ? stat.getAvgAmmoniaNitrogen().doubleValue() : null);
            waterTemperature.add(stat.getAvgWaterTemperature() != null ? stat.getAvgWaterTemperature().doubleValue() : null);
            phValue.add(stat.getAvgPhValue() != null ? stat.getAvgPhValue().doubleValue() : null);
            conductivity.add(stat.getAvgConductivity() != null ? stat.getAvgConductivity().doubleValue() : null);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("dissolved_oxygen", dissolvedOxygen);
        result.put("ammonia_nitrogen", ammoniaNitrogen);
        result.put("water_temperature", waterTemperature);
        result.put("ph_value", phValue);
        result.put("conductivity", conductivity);
        return result;
    }

    /**
     * 从日统计数据构建趋势数据
     */
    private Map<String, Object> buildTrendDataFromDailyStats(List<AgricultureWaterQualityDailyStats> stats, String range) {
        List<String> xAxis = new ArrayList<>();
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> ammoniaNitrogen = new ArrayList<>();
        List<Double> waterTemperature = new ArrayList<>();
        List<Double> phValue = new ArrayList<>();
        List<Double> conductivity = new ArrayList<>();

        String timeFormat = "MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        
        for (AgricultureWaterQualityDailyStats stat : stats) {
            xAxis.add(stat.getStatDate().format(formatter));
            dissolvedOxygen.add(stat.getAvgDissolvedOxygen() != null ? stat.getAvgDissolvedOxygen().doubleValue() : null);
            ammoniaNitrogen.add(stat.getAvgAmmoniaNitrogen() != null ? stat.getAvgAmmoniaNitrogen().doubleValue() : null);
            waterTemperature.add(stat.getAvgWaterTemperature() != null ? stat.getAvgWaterTemperature().doubleValue() : null);
            phValue.add(stat.getAvgPhValue() != null ? stat.getAvgPhValue().doubleValue() : null);
            conductivity.add(stat.getAvgConductivity() != null ? stat.getAvgConductivity().doubleValue() : null);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("dissolved_oxygen", dissolvedOxygen);
        result.put("ammonia_nitrogen", ammoniaNitrogen);
        result.put("water_temperature", waterTemperature);
        result.put("ph_value", phValue);
        result.put("conductivity", conductivity);
        return result;
    }

    /**
     * 从原始数据获取趋势数据（备用方案）
     */
    private Map<String, Object> getTrendDataFromRawData(String pastureId, String batchId, String range) {
        // 1. 计算时间范围
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        int points;
        String timeFormat;
        if ("day".equals(range)) {
            start = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
            points = 24;
            timeFormat = "HH:00";
        } else if ("week".equals(range)) {
            start = now.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
            points = 7;
            timeFormat = "MM-dd";
        } else { // month
            start = now.minusDays(29).withHour(0).withMinute(0).withSecond(0).withNano(0);
            points = 30;
            timeFormat = "MM-dd";
        }

        // 2. 查询数据
        List<AgricultureWaterQualityData> allData = this.lambdaQuery()
                .eq(AgricultureWaterQualityData::getPastureId, pastureId)
                .eq(AgricultureWaterQualityData::getBatchId, batchId)
                .ge(AgricultureWaterQualityData::getCollectTime, start)
                .le(AgricultureWaterQualityData::getCollectTime, now)
                .orderByAsc(AgricultureWaterQualityData::getCollectTime)
                .list();

        // 3. 分桶聚合
        Map<String, List<AgricultureWaterQualityData>> bucketMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        for (int i = 0; i < points; i++) {
            LocalDateTime bucketTime;
            if ("day".equals(range)) {
                bucketTime = start.plusHours(i);
            } else {
                bucketTime = start.plusDays(i);
            }
            String key = bucketTime.format(formatter);
            bucketMap.put(key, new ArrayList<>());
        }
        for (AgricultureWaterQualityData d : allData) {
            LocalDateTime t = d.getCollectTime();
            String key;
            if ("day".equals(range)) {
                key = t.withMinute(0).withSecond(0).withNano(0).format(formatter);
            } else {
                key = t.withHour(0).withMinute(0).withSecond(0).withNano(0).format(formatter);
            }
            if (bucketMap.containsKey(key)) {
                bucketMap.get(key).add(d);
            }
        }

        // 4. 计算每个桶的平均值（只统计水质5个字段）
        List<String> xAxis = new ArrayList<>(bucketMap.keySet());
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> ammoniaNitrogen = new ArrayList<>();
        List<Double> waterTemperature = new ArrayList<>();
        List<Double> phValue = new ArrayList<>();
        List<Double> conductivity = new ArrayList<>();

        for (List<AgricultureWaterQualityData> bucket : bucketMap.values()) {
            dissolvedOxygen.add(average(bucket, AgricultureWaterQualityData::getDissolvedOxygen));
            ammoniaNitrogen.add(average(bucket, AgricultureWaterQualityData::getAmmoniaNitrogen));
            waterTemperature.add(average(bucket, AgricultureWaterQualityData::getWaterTemperature));
            phValue.add(average(bucket, AgricultureWaterQualityData::getPhValue));
            conductivity.add(average(bucket, AgricultureWaterQualityData::getConductivity));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("dissolved_oxygen", dissolvedOxygen);
        result.put("ammonia_nitrogen", ammoniaNitrogen);
        result.put("water_temperature", waterTemperature);
        result.put("ph_value", phValue);
        result.put("conductivity", conductivity);
        return result;
    }

    /**
     * 计算平均值（忽略null）
     */
    private Double average(List<AgricultureWaterQualityData> list, Function<AgricultureWaterQualityData, Double> getter) {
        if (list == null || list.isEmpty()) return null;
        List<Double> values = list.stream().map(getter).filter(Objects::nonNull).collect(Collectors.toList());
        if (values.isEmpty()) return null;
        double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return round(avg, 2);
    }

    /**
     * 新增水质数据
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 结果
     */
    @Override
    public int insertAgricultureWaterQualityData(AgricultureWaterQualityData agricultureWaterQualityData)
    {

        return agricultureWaterQualityDataMapper.insert(agricultureWaterQualityData);
    }

    /**
     * 修改水质数据
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 结果
     */
    @Override
    public int updateAgricultureWaterQualityData(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return updateById(agricultureWaterQualityData) ? 1 : 0;
    }

    /**
     * 批量删除水质数据
     * 
     * @param ids 需要删除的水质数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWaterQualityDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除水质数据信息
     * 
     * @param id 水质数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWaterQualityDataById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
