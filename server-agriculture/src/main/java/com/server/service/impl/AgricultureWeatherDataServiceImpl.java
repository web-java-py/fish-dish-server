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
import com.server.domain.AgricultureWeatherData;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureWeatherDataMapper;
import com.server.service.AgricultureWeatherDataService;
import com.server.domain.AgricultureWeatherDailyStats;
import com.server.domain.AgricultureWeatherHourlyStats;
import com.server.mapper.AgricultureWeatherDailyStatsMapper;
import com.server.mapper.AgricultureWeatherHourlyStatsMapper;
import com.server.service.AgricultureWeatherDailyStatsService;
import com.server.service.AgricultureWeatherHourlyStatsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

/**
 * 气象数据Service业务层处理
 * 
 * @author server
 * @date 2025-06-08
 */
@Service
public class AgricultureWeatherDataServiceImpl extends ServiceImpl<AgricultureWeatherDataMapper, AgricultureWeatherData> implements AgricultureWeatherDataService
{
    @Autowired
    private AgricultureWeatherDataMapper agricultureWeatherDataMapper;

    @Autowired
    private AgricultureWeatherDailyStatsService dailyStatsService;

    @Autowired
    private AgricultureWeatherHourlyStatsService hourlyStatsService;

    /**
     * 查询气象数据
     * 
     * @param id 气象数据主键
     * @return 气象数据
     */
    @Override
    public AgricultureWeatherData selectAgricultureWeatherDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询气象数据列表
     * 
     * @param agricultureWeatherData 气象数据
     * @return 气象数据
     */
    @Override
    public List<AgricultureWeatherData> selectAgricultureWeatherDataList(AgricultureWeatherData agricultureWeatherData)
    {
        return list();
    }

    /**
     * 新增气象数据
     * 
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    @Override
    public int insertAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData)
    {
        return agricultureWeatherDataMapper.insert(agricultureWeatherData);
    }

    /**
     * 修改气象数据
     * 
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    @Override
    public int updateAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData)
    {
        return  updateById(agricultureWeatherData) ? 1 : 0;
    }

    /**
     * 批量删除气象数据
     * 
     * @param ids 需要删除的气象数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWeatherDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除气象数据信息
     * 
     * @param id 气象数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWeatherDataById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }

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
        List<AgricultureWeatherHourlyStats> stats = hourlyStatsService.getHourlyStats(pastureId, batchId, start, now);
        
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
        List<AgricultureWeatherDailyStats> stats = dailyStatsService.getDailyStats(pastureId, batchId, start, now);
        
        if (!stats.isEmpty()) {
            return buildTrendDataFromDailyStats(stats, range);
        }
        
        // 统计表没有数据，查询原始表
        return getTrendDataFromRawData(pastureId, batchId, range);
    }

    /**
     * 从小时统计表构建趋势数据
     */
    private Map<String, Object> buildTrendDataFromHourlyStats(List<AgricultureWeatherHourlyStats> stats) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
        
        // 1. 生成实际时间范围的xAxis和默认值
        List<String> xAxis = new ArrayList<>();
        List<Double> temperature = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();
        List<Double> lightIntensity = new ArrayList<>();
    
        // 生成从start到now的每个小时
        LocalDateTime currentHour = start;
        while (!currentHour.isAfter(now)) {
            xAxis.add(currentHour.format(DateTimeFormatter.ofPattern("HH:mm")));
            temperature.add(0.0);
            humidity.add(0.0);
            windSpeed.add(0.0);
            lightIntensity.add(0.0);
            currentHour = currentHour.plusHours(1);
        }
    
        // 2. 用有数据的小时覆盖
        for (AgricultureWeatherHourlyStats stat : stats) {
            // 找到对应的索引位置
            int index = -1;
            for (int i = 0; i < xAxis.size(); i++) {
                LocalDateTime hourTime = start.plusHours(i);
                if (hourTime.getHour() == stat.getStatHour().getHour() && 
                    hourTime.toLocalDate().equals(stat.getStatHour().toLocalDate())) {
                    index = i;
                    break;
                }
            }
            
            if (index >= 0 && index < xAxis.size()) {
                temperature.set(index, stat.getAvgTemperature() == null ? 0.0 : stat.getAvgTemperature().doubleValue());
                humidity.set(index, stat.getAvgHumidity() == null ? 0.0 : stat.getAvgHumidity().doubleValue());
                windSpeed.set(index, stat.getAvgWindSpeed() == null ? 0.0 : stat.getAvgWindSpeed().doubleValue());
                lightIntensity.set(index, stat.getAvgLightIntensity() == null ? 0.0 : stat.getAvgLightIntensity().doubleValue());
            }
        }
    
        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("temperature", temperature);
        result.put("humidity", humidity);
        result.put("windSpeed", windSpeed);
        result.put("lightIntensity", lightIntensity);
        return result;
    }

    /**
     * 从日统计表构建趋势数据
     */
    private Map<String, Object> buildTrendDataFromDailyStats(List<AgricultureWeatherDailyStats> stats, String range) {
        int days = "week".equals(range) ? 7 : 30;

        List<String> xAxis = new ArrayList<>();
        List<Double> temperature = new ArrayList<>(Collections.nCopies(days, 0.0));
        List<Double> humidity = new ArrayList<>(Collections.nCopies(days, 0.0));
        List<Double> windSpeed = new ArrayList<>(Collections.nCopies(days, 0.0));
        List<Double> lightIntensity = new ArrayList<>(Collections.nCopies(days, 0.0));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(days - 1);

        Map<String, AgricultureWeatherDailyStats> statMap = new HashMap<>();
        for (AgricultureWeatherDailyStats stat : stats) {
            String key = stat.getStatDate().format(DateTimeFormatter.ofPattern("MM-dd"));
            statMap.put(key, stat);
        }

        for (int i = 0; i < days; i++) {
            LocalDate date = startDate.plusDays(i);
            String key = date.format(DateTimeFormatter.ofPattern("MM-dd"));
            xAxis.add(key);

            AgricultureWeatherDailyStats stat = statMap.get(key);
            if (stat != null) {
                temperature.set(i, stat.getAvgTemperature() == null ? 0.0 : stat.getAvgTemperature().doubleValue());
                humidity.set(i, stat.getAvgHumidity() == null ? 0.0 : stat.getAvgHumidity().doubleValue());
                windSpeed.set(i, stat.getAvgWindSpeed() == null ? 0.0 : stat.getAvgWindSpeed().doubleValue());
                lightIntensity.set(i, stat.getAvgLightIntensity() == null ? 0.0 : stat.getAvgLightIntensity().doubleValue());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("temperature", temperature);
        result.put("humidity", humidity);
        result.put("windSpeed", windSpeed);
        result.put("lightIntensity", lightIntensity);
        return result;
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
        List<AgricultureWeatherData> allData = this.lambdaQuery()
                .eq(AgricultureWeatherData::getPastureId, pastureId)
                .eq(AgricultureWeatherData::getBatchId, batchId)
                .ge(AgricultureWeatherData::getCollectTime, start)
                .le(AgricultureWeatherData::getCollectTime, now)
                .orderByAsc(AgricultureWeatherData::getCollectTime)
                .list();

        // 3. 先合并同采集时间的数据
        List<AgricultureWeatherData> mergedData = mergeSameTimeData(allData);

        // 4. 分桶聚合
        Map<String, List<AgricultureWeatherData>> bucketMap = new LinkedHashMap<>();
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
        for (AgricultureWeatherData d : mergedData) {
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

        // 5. 计算每个桶的平均值
        List<String> xAxis = new ArrayList<>(bucketMap.keySet());
        List<Double> temperature = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();
        List<Double> lightIntensity = new ArrayList<>();

        for (List<AgricultureWeatherData> bucket : bucketMap.values()) {
            temperature.add(calculateAverage(bucket, AgricultureWeatherData::getTemperature, "temperature"));
            humidity.add(calculateAverage(bucket, AgricultureWeatherData::getHumidity, "humidity"));
            windSpeed.add(calculateAverage(bucket, AgricultureWeatherData::getWindSpeed, "windSpeed"));
            lightIntensity.add(calculateAverage(bucket, AgricultureWeatherData::getLightIntensity, "lightIntensity"));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("temperature", temperature);
        result.put("humidity", humidity);
        result.put("windSpeed", windSpeed);
        result.put("lightIntensity", lightIntensity);
        return result;
    }

    /**
     * 合并同一采集时间的多设备数据为一条完整数据
     */
    private List<AgricultureWeatherData> mergeSameTimeData(List<AgricultureWeatherData> dataList) {
        // 按采集时间分组
        Map<LocalDateTime, List<AgricultureWeatherData>> grouped = dataList.stream()
                .collect(Collectors.groupingBy(AgricultureWeatherData::getCollectTime, LinkedHashMap::new, Collectors.toList()));

        List<AgricultureWeatherData> mergedList = new ArrayList<>();
        for (Map.Entry<LocalDateTime, List<AgricultureWeatherData>> entry : grouped.entrySet()) {
            List<AgricultureWeatherData> group = entry.getValue();
            AgricultureWeatherData merged = new AgricultureWeatherData();
            merged.setCollectTime(entry.getKey());
            merged.setPastureId(group.get(0).getPastureId());
            merged.setBatchId(group.get(0).getBatchId());
            // 合并各参数
            for (AgricultureWeatherData d : group) {
                if (d.getTemperature() != null) merged.setTemperature(d.getTemperature());
                if (d.getHumidity() != null) merged.setHumidity(d.getHumidity());
                if (d.getWindSpeed() != null) merged.setWindSpeed(d.getWindSpeed());
                if (d.getLightIntensity() != null) merged.setLightIntensity(d.getLightIntensity());
                // 风向不用合并
            }
            mergedList.add(merged);
        }
        return mergedList;
    }

    /**
     * 计算平均值（忽略无效数据）
     * @param dataList 数据列表
     * @param valueGetter 获取字段值的方法引用
     * @param fieldName 字段名（用于特殊处理风速等）
     * @return 平均值，若无有效数据则返回 null
     */
    private Double calculateAverage(List<AgricultureWeatherData> dataList, Function<AgricultureWeatherData, Double> valueGetter, String fieldName) {
        if (dataList == null || dataList.isEmpty()) return null;

        // 过滤掉4个字段全为0或null的无效数据
        List<AgricultureWeatherData> filteredList = dataList.stream()
                .filter(data -> {
                    Double temperature = data.getTemperature();
                    Double humidity = data.getHumidity();
                    Double windSpeed = data.getWindSpeed();
                    Double lightIntensity = data.getLightIntensity();
                    // 全为null或0时，过滤掉
                    boolean allFieldsZeroOrNull =
                            (temperature == null || temperature == 0)
                                    && (humidity == null || humidity == 0)
                                    && (windSpeed == null || windSpeed == 0)
                                    && (lightIntensity == null || lightIntensity == 0);
                    return !allFieldsZeroOrNull;
                })
                .collect(Collectors.toList());

        if (filteredList.isEmpty()) return null;

        List<Double> validValues;
        switch (fieldName) {
            case "windSpeed":
                // 风速为0可以，null不行
                validValues = filteredList.stream()
                        .map(AgricultureWeatherData::getWindSpeed)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                break;
            case "temperature":
                validValues = filteredList.stream()
                        .map(AgricultureWeatherData::getTemperature)
                        .filter(value -> value != null && value != 0)
                        .collect(Collectors.toList());
                break;
            case "humidity":
                validValues = filteredList.stream()
                        .map(AgricultureWeatherData::getHumidity)
                        .filter(value -> value != null && value != 0)
                        .collect(Collectors.toList());
                break;
            case "lightIntensity":
                validValues = filteredList.stream()
                        .map(AgricultureWeatherData::getLightIntensity)
                        .filter(value -> value != null && value != 0)
                        .collect(Collectors.toList());
                break;
            default:
                // 默认行为
                validValues = filteredList.stream()
                        .map(valueGetter)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
        }

        if (validValues.isEmpty()) return null;
        double avg = validValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return round(avg, 2);
    }

    private Double round(Double value, int scale) {
        if (value == null) return null;
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}
