package com.server.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
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
            Map<String, Object> result = getHourlyTrendData(pastureId, batchId);
            // 检查是否有数据，如果没有则回退到原始数据
            List<Double> dissolvedOxygen = (List<Double>) result.get("dissolved_oxygen");
            if (dissolvedOxygen != null && dissolvedOxygen.stream().allMatch(v -> v == 0.0)) {
                return getTrendDataFromRawData(pastureId, batchId, range);
            }
            return result;
        } else {
            Map<String, Object> result = getDailyTrendData(pastureId, batchId, range);
            // 检查是否有数据，如果没有则回退到原始数据
            List<Double> dissolvedOxygen = (List<Double>) result.get("dissolved_oxygen");
            if (dissolvedOxygen != null && dissolvedOxygen.stream().allMatch(v -> v == 0.0)) {
                return getTrendDataFromRawData(pastureId, batchId, range);
            }
            return result;
        }
    }

    /**
     * 获取小时趋势数据（优先查询统计表，补充当前小时数据）
     */
    private Map<String, Object> getHourlyTrendData(String pastureId, String batchId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
        
        // 查询统计表数据（历史数据）
        List<AgricultureWaterQualityHourlyStats> stats = hourlyStatsService.getHourlyStats(pastureId, batchId, start, now);
        
        // 查询当前小时的原始数据
        LocalDateTime currentHour = now.withMinute(0).withSecond(0).withNano(0);
        
        // 检查统计表中是否已经包含当前小时的数据
        boolean hasCurrentHourData = stats.stream()
                .anyMatch(stat -> stat.getStatHour().equals(currentHour));
        
        // 如果统计表中没有当前小时的数据，则查询原始数据并计算
        if (!hasCurrentHourData) {
            List<AgricultureWaterQualityData> currentHourData = this.lambdaQuery()
                    .eq(AgricultureWaterQualityData::getPastureId, pastureId)
                    .eq(AgricultureWaterQualityData::getBatchId, batchId)
                    .ge(AgricultureWaterQualityData::getCollectTime, currentHour)
                    .lt(AgricultureWaterQualityData::getCollectTime, currentHour.plusHours(1))
                    .list();
            
            // 如果有当前小时的数据，计算平均值并添加到统计列表中
            if (!currentHourData.isEmpty()) {
                AgricultureWaterQualityHourlyStats currentHourStats = calculateWaterQualityHourlyStatsFromRawData(currentHourData, currentHour);
                if (currentHourStats != null) {
                    stats.add(currentHourStats);
                }
            }
        }
        
        return buildTrendDataFromHourlyStats(stats);
    }

    /**
     * 获取日趋势数据（优先查询统计表，补充今天的数据）
     */
    private Map<String, Object> getDailyTrendData(String pastureId, String batchId, String range) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start;
        int days;
        
        if ("week".equals(range)) {
            start = now.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
            days = 7;
        } else { // month
            start = now.minusDays(29).withHour(0).withMinute(0).withSecond(0).withNano(0);
            days = 30;
        }
        
        // 查询统计表数据（历史数据）
        List<AgricultureWaterQualityDailyStats> stats = dailyStatsService.getDailyStats(pastureId, batchId, start, now);
        
        // 查询今天的原始数据
        LocalDate today = now.toLocalDate();
        
        // 检查统计表中是否已经包含今天的数据
        boolean hasTodayData = stats.stream()
                .anyMatch(stat -> stat.getStatDate().equals(today));
        
        // 如果统计表中没有今天的数据，则查询原始数据并计算
        if (!hasTodayData) {
            List<AgricultureWaterQualityData> todayData = this.lambdaQuery()
                    .eq(AgricultureWaterQualityData::getPastureId, pastureId)
                    .eq(AgricultureWaterQualityData::getBatchId, batchId)
                    .ge(AgricultureWaterQualityData::getCollectTime, today.atStartOfDay())
                    .le(AgricultureWaterQualityData::getCollectTime, today.atTime(23, 59, 59, 999999999))
                    .list();
            
            // 如果有今天的数据，计算平均值并添加到统计列表中
            if (!todayData.isEmpty()) {
                AgricultureWaterQualityDailyStats todayStats = calculateWaterQualityDailyStatsFromRawData(todayData, today);
                if (todayStats != null) {
                    stats.add(todayStats);
                }
            }
        }
        
        return buildTrendDataFromDailyStats(stats, range, start, days);
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
        
        // 生成完整的24小时时间轴
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
        
        // 将统计数据转换为Map，方便查找
        Map<String, AgricultureWaterQualityHourlyStats> statsMap = new HashMap<>();
        for (AgricultureWaterQualityHourlyStats stat : stats) {
            String key = stat.getStatHour().format(formatter);
            statsMap.put(key, stat);
        }
        
        // 生成完整的24小时数据
        for (int i = 0; i < 24; i++) {
            LocalDateTime hourTime = start.plusHours(i);
            String hourKey = hourTime.format(formatter);
            xAxis.add(hourKey);
            
            // 查找该小时的统计数据
            AgricultureWaterQualityHourlyStats stat = statsMap.get(hourKey);
            if (stat != null) {
                dissolvedOxygen.add(stat.getAvgDissolvedOxygen() != null ? stat.getAvgDissolvedOxygen().doubleValue() : 0.0);
                ammoniaNitrogen.add(stat.getAvgAmmoniaNitrogen() != null ? stat.getAvgAmmoniaNitrogen().doubleValue() : 0.0);
                waterTemperature.add(stat.getAvgWaterTemperature() != null ? stat.getAvgWaterTemperature().doubleValue() : 0.0);
                phValue.add(stat.getAvgPhValue() != null ? stat.getAvgPhValue().doubleValue() : 0.0);
                conductivity.add(stat.getAvgConductivity() != null ? stat.getAvgConductivity().doubleValue() : 0.0);
            } else {
                // 没有数据的时段填充为0
                dissolvedOxygen.add(0.0);
                ammoniaNitrogen.add(0.0);
                waterTemperature.add(0.0);
                phValue.add(0.0);
                conductivity.add(0.0);
            }
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
    private Map<String, Object> buildTrendDataFromDailyStats(List<AgricultureWaterQualityDailyStats> stats, String range, LocalDateTime start, int days) {
        List<String> xAxis = new ArrayList<>();
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> ammoniaNitrogen = new ArrayList<>();
        List<Double> waterTemperature = new ArrayList<>();
        List<Double> phValue = new ArrayList<>();
        List<Double> conductivity = new ArrayList<>();

        String timeFormat = "MM-dd";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(timeFormat);
        
        // 将统计数据转换为Map，使用LinkedHashMap并处理重复键
        Map<LocalDate, AgricultureWaterQualityDailyStats> statsMap = new LinkedHashMap<>();
        for (AgricultureWaterQualityDailyStats stat : stats) {
            // 如果有重复键，保留最新的数据（通常是动态计算的）
            statsMap.put(stat.getStatDate(), stat);
        }
        
        // 生成完整的日期范围
        for (int i = 0; i < days; i++) {
            LocalDateTime currentDateTime = start.plusDays(i);
            LocalDate currentDate = currentDateTime.toLocalDate();
            String dateStr = currentDateTime.format(formatter);
            xAxis.add(dateStr);
            
            // 查找该日期的统计数据
            AgricultureWaterQualityDailyStats stat = statsMap.get(currentDate);
            if (stat != null) {
                dissolvedOxygen.add(stat.getAvgDissolvedOxygen() != null ? stat.getAvgDissolvedOxygen().doubleValue() : 0.0);
                ammoniaNitrogen.add(stat.getAvgAmmoniaNitrogen() != null ? stat.getAvgAmmoniaNitrogen().doubleValue() : 0.0);
                waterTemperature.add(stat.getAvgWaterTemperature() != null ? stat.getAvgWaterTemperature().doubleValue() : 0.0);
                phValue.add(stat.getAvgPhValue() != null ? stat.getAvgPhValue().doubleValue() : 0.0);
                conductivity.add(stat.getAvgConductivity() != null ? stat.getAvgConductivity().doubleValue() : 0.0);
            } else {
                // 没有数据的日期设置为0
                dissolvedOxygen.add(0.0);
                ammoniaNitrogen.add(0.0);
                waterTemperature.add(0.0);
                phValue.add(0.0);
                conductivity.add(0.0);
            }
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
     * 从原始数据计算水质小时统计数据
     */
    private AgricultureWaterQualityHourlyStats calculateWaterQualityHourlyStatsFromRawData(List<AgricultureWaterQualityData> dataList, LocalDateTime hour) {
        if (dataList.isEmpty()) {
            return null;
        }
        
        // 计算平均值
        BigDecimal avgPhValue = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getPhValue);
        BigDecimal avgDissolvedOxygen = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getDissolvedOxygen);
        BigDecimal avgAmmoniaNitrogen = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getAmmoniaNitrogen);
        BigDecimal avgWaterTemperature = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getWaterTemperature);
        BigDecimal avgConductivity = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getConductivity);
        
        return AgricultureWaterQualityHourlyStats.builder()
                .pastureId(dataList.get(0).getPastureId())
                .batchId(dataList.get(0).getBatchId())
                .statHour(hour)
                .avgPhValue(avgPhValue)
                .avgDissolvedOxygen(avgDissolvedOxygen)
                .avgAmmoniaNitrogen(avgAmmoniaNitrogen)
                .avgWaterTemperature(avgWaterTemperature)
                .avgConductivity(avgConductivity)
                .dataCount(dataList.size())
                .createTime(LocalDateTime.now())
                .build();
    }

    /**
     * 从原始数据计算水质日统计数据
     */
    private AgricultureWaterQualityDailyStats calculateWaterQualityDailyStatsFromRawData(List<AgricultureWaterQualityData> dataList, LocalDate date) {
        if (dataList.isEmpty()) {
            return null;
        }
        
        // 计算平均值
        BigDecimal avgPhValue = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getPhValue);
        BigDecimal avgDissolvedOxygen = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getDissolvedOxygen);
        BigDecimal avgAmmoniaNitrogen = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getAmmoniaNitrogen);
        BigDecimal avgWaterTemperature = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getWaterTemperature);
        BigDecimal avgConductivity = calculateWaterQualityAverage(dataList, AgricultureWaterQualityData::getConductivity);
        
        return AgricultureWaterQualityDailyStats.builder()
                .pastureId(dataList.get(0).getPastureId())
                .batchId(dataList.get(0).getBatchId())
                .statDate(date)
                .avgPhValue(avgPhValue)
                .avgDissolvedOxygen(avgDissolvedOxygen)
                .avgAmmoniaNitrogen(avgAmmoniaNitrogen)
                .avgWaterTemperature(avgWaterTemperature)
                .avgConductivity(avgConductivity)
                .dataCount(dataList.size())
                .createTime(LocalDateTime.now())
                .build();
    }

    /**
     * 计算水质数据平均值
     */
    private BigDecimal calculateWaterQualityAverage(List<AgricultureWaterQualityData> dataList, Function<AgricultureWaterQualityData, Double> getter) {
        List<Double> values = dataList.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        
        if (values.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        double avg = values.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
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

    @Override
    public AgricultureWaterQualityData getLatestWaterQualityData(String pastureId) {
        return this.lambdaQuery()
                .eq(AgricultureWaterQualityData::getPastureId, pastureId)
                .orderByDesc(AgricultureWaterQualityData::getCollectTime)
                .last("limit 1")
                .one();
    }

    /**
     * 从原始数据获取趋势数据
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
        
        // 生成完整的时间轴
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
        
        // 将数据分配到对应的桶中
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

        // 4. 计算每个桶的平均值，确保返回完整的时间范围
        List<String> xAxis = new ArrayList<>();
        List<Double> dissolvedOxygen = new ArrayList<>();
        List<Double> ammoniaNitrogen = new ArrayList<>();
        List<Double> waterTemperature = new ArrayList<>();
        List<Double> phValue = new ArrayList<>();
        List<Double> conductivity = new ArrayList<>();

        for (Map.Entry<String, List<AgricultureWaterQualityData>> entry : bucketMap.entrySet()) {
            String key = entry.getKey();
            List<AgricultureWaterQualityData> bucket = entry.getValue();
            
            xAxis.add(key);
            dissolvedOxygen.add(calculateWaterQualityAverage(bucket, AgricultureWaterQualityData::getDissolvedOxygen).doubleValue());
            ammoniaNitrogen.add(calculateWaterQualityAverage(bucket, AgricultureWaterQualityData::getAmmoniaNitrogen).doubleValue());
            waterTemperature.add(calculateWaterQualityAverage(bucket, AgricultureWaterQualityData::getWaterTemperature).doubleValue());
            phValue.add(calculateWaterQualityAverage(bucket, AgricultureWaterQualityData::getPhValue).doubleValue());
            conductivity.add(calculateWaterQualityAverage(bucket, AgricultureWaterQualityData::getConductivity).doubleValue());
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
}
