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
public class AgricultureWeatherDataServiceImpl extends ServiceImpl<AgricultureWeatherDataMapper, AgricultureWeatherData> implements AgricultureWeatherDataService {
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
    public AgricultureWeatherData selectAgricultureWeatherDataById(Long id) {
        return getById(id);
    }

    /**
     * 查询气象数据列表
     *
     * @param agricultureWeatherData 气象数据
     * @return 气象数据
     */
    @Override
    public List<AgricultureWeatherData> selectAgricultureWeatherDataList(AgricultureWeatherData agricultureWeatherData) {
        return list();
    }

    /**
     * 新增气象数据
     *
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    @Override
    public int insertAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData) {
        return agricultureWeatherDataMapper.insert(agricultureWeatherData);
    }

    /**
     * 修改气象数据
     *
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    @Override
    public int updateAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData) {
        return updateById(agricultureWeatherData) ? 1 : 0;
    }

    /**
     * 批量删除气象数据
     *
     * @param ids 需要删除的气象数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWeatherDataByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除气象数据信息
     *
     * @param id 气象数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWeatherDataById(Long id) {
        return removeById(id) ? 1 : 0;
    }

    @Override
    public Map<String, Object> getTrendData(String pastureId, String batchId, String range) {
        // 优先查询统计表，如果没有数据再查询原始表
        if ("day".equals(range)) {
            Map<String, Object> result = getHourlyTrendData(pastureId, batchId);
            // 检查是否有数据，如果没有则回退到原始数据
            List<Double> temperature = (List<Double>) result.get("temperature");
            if (temperature != null && temperature.stream().allMatch(v -> v == 0.0)) {
                return getTrendDataFromRawData(pastureId, batchId, range);
            }
            return result;
        } else {
            Map<String, Object> result = getDailyTrendData(pastureId, batchId, range);
            // 检查是否有数据，如果没有则回退到原始数据
            List<Double> temperature = (List<Double>) result.get("temperature");
            if (temperature != null && temperature.stream().allMatch(v -> v == 0.0)) {
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
        List<AgricultureWeatherHourlyStats> stats = hourlyStatsService.getHourlyStats(pastureId, batchId, start, now);

        // 查询当前小时的原始数据
        LocalDateTime currentHour = now.withMinute(0).withSecond(0).withNano(0);
        List<AgricultureWeatherData> currentHourData = this.lambdaQuery()
                .eq(AgricultureWeatherData::getPastureId, pastureId)
                .eq(AgricultureWeatherData::getBatchId, batchId)
                .ge(AgricultureWeatherData::getCollectTime, currentHour)
                .lt(AgricultureWeatherData::getCollectTime, currentHour.plusHours(1))
                .list();

        // 先移除已有的当前小时
        stats.removeIf(stat -> stat.getStatHour().equals(currentHour));

        // 如果有当前小时的数据，计算平均值并添加到统计列表中
        if (!currentHourData.isEmpty()) {
            AgricultureWeatherHourlyStats currentHourStats = calculateHourlyStatsFromRawData(currentHourData, currentHour);
            stats.add(currentHourStats);
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
        List<AgricultureWeatherDailyStats> stats = dailyStatsService.getDailyStats(pastureId, batchId, start, now);

        // 查询今天的原始数据
        LocalDate today = now.toLocalDate();
        List<AgricultureWeatherData> todayData = this.lambdaQuery()
                .eq(AgricultureWeatherData::getPastureId, pastureId)
                .eq(AgricultureWeatherData::getBatchId, batchId)
                .ge(AgricultureWeatherData::getCollectTime, today.atStartOfDay())
                .le(AgricultureWeatherData::getCollectTime, today.atTime(23, 59, 59, 999999999))
                .list();

        // 如果有今天的数据，计算平均值并添加到统计列表中
        if (!todayData.isEmpty()) {
            AgricultureWeatherDailyStats todayStats = calculateDailyStatsFromRawData(todayData, today);
            stats.add(todayStats);
        }

        return buildTrendDataFromDailyStats(stats, range, start, days);
    }

    /**
     * 从小时统计表构建趋势数据
     */
    private Map<String, Object> buildTrendDataFromHourlyStats(List<AgricultureWeatherHourlyStats> stats) {
        List<String> xAxis = new ArrayList<>();
        List<Double> temperature = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();
        List<Double> lightIntensity = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:00");
        
        // 生成完整的24小时时间轴
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusHours(23).withMinute(0).withSecond(0).withNano(0);
        
        // 将统计数据转换为Map，方便查找
        Map<String, AgricultureWeatherHourlyStats> statsMap = new HashMap<>();
        for (AgricultureWeatherHourlyStats stat : stats) {
            String key = stat.getStatHour().format(formatter);
            statsMap.put(key, stat);
        }
        
        // 生成完整的24小时数据
        for (int i = 0; i < 24; i++) {
            LocalDateTime hourTime = start.plusHours(i);
            String hourKey = hourTime.format(formatter);
            xAxis.add(hourKey);
            
            // 查找该小时的统计数据
            AgricultureWeatherHourlyStats stat = statsMap.get(hourKey);
            if (stat != null) {
                temperature.add(stat.getAvgTemperature() != null ? stat.getAvgTemperature().doubleValue() : 0.0);
                humidity.add(stat.getAvgHumidity() != null ? stat.getAvgHumidity().doubleValue() : 0.0);
                windSpeed.add(stat.getAvgWindSpeed() != null ? stat.getAvgWindSpeed().doubleValue() : 0.0);
                lightIntensity.add(stat.getAvgLightIntensity() != null ? stat.getAvgLightIntensity().doubleValue() : 0.0);
            } else {
                // 没有数据的时段填充为0
                temperature.add(0.0);
                humidity.add(0.0);
                windSpeed.add(0.0);
                lightIntensity.add(0.0);
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
    private Map<String, Object> buildTrendDataFromDailyStats(List<AgricultureWeatherDailyStats> stats, String range, LocalDateTime start, int days) {
        int expectedDays = "week".equals(range) ? 7 : 30;

        List<String> xAxis = new ArrayList<>();
        List<Double> temperature = new ArrayList<>(Collections.nCopies(expectedDays, 0.0));
        List<Double> humidity = new ArrayList<>(Collections.nCopies(expectedDays, 0.0));
        List<Double> windSpeed = new ArrayList<>(Collections.nCopies(expectedDays, 0.0));
        List<Double> lightIntensity = new ArrayList<>(Collections.nCopies(expectedDays, 0.0));

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(expectedDays - 1);

        Map<String, AgricultureWeatherDailyStats> statMap = new HashMap<>();
        for (AgricultureWeatherDailyStats stat : stats) {
            String key = stat.getStatDate().format(DateTimeFormatter.ofPattern("MM-dd"));
            statMap.put(key, stat);
        }

        for (int i = 0; i < expectedDays; i++) {
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

        // 5. 计算每个桶的平均值，确保返回完整的时间范围
        List<String> xAxis = new ArrayList<>();
        List<Double> temperature = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();
        List<Double> lightIntensity = new ArrayList<>();

        for (Map.Entry<String, List<AgricultureWeatherData>> entry : bucketMap.entrySet()) {
            String key = entry.getKey();
            List<AgricultureWeatherData> bucket = entry.getValue();
            
            xAxis.add(key);
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
     * 合并同一采集时间的多设备数据为一条完整数据 带风向
     */
    private List<AgricultureWeatherData> mergeSameTimeDatatAndWindDirection(List<AgricultureWeatherData> dataList) {
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
                if (merged.getTemperature() == null && d.getTemperature() != null && d.getTemperature() != 0) merged.setTemperature(d.getTemperature());
                if (merged.getHumidity() == null && d.getHumidity() != null && d.getHumidity() != 0) merged.setHumidity(d.getHumidity());
                if (merged.getWindSpeed() == null && d.getWindSpeed() != null) merged.setWindSpeed(d.getWindSpeed()); // 允许为0
                if (merged.getLightIntensity() == null && d.getLightIntensity() != null && d.getLightIntensity() != 0) merged.setLightIntensity(d.getLightIntensity());
                if (merged.getRainfall() == null && d.getRainfall() != null && d.getRainfall() != 0) merged.setRainfall(d.getRainfall());
                if (merged.getWindDirection() == null && d.getWindDirection() != null && !d.getWindDirection().isEmpty()) merged.setWindDirection(d.getWindDirection());
            }
            mergedList.add(merged);
        }
        return mergedList;
    }

    /**
     * 计算平均值（忽略无效数据）
     *
     * @param dataList    数据列表
     * @param valueGetter 获取字段值的方法引用
     * @param fieldName   字段名（用于特殊处理风速等）
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

    /**
     * 从原始数据计算小时统计数据
     */
    private AgricultureWeatherHourlyStats calculateHourlyStatsFromRawData(List<AgricultureWeatherData> dataList, LocalDateTime hour) {
        if (dataList.isEmpty()) {
            return null;
        }

        // 合并同采集时间的数据
        List<AgricultureWeatherData> mergedData = mergeSameTimeData(dataList);

        // 计算平均值
        BigDecimal avgTemperature = calculateAverage(mergedData, AgricultureWeatherData::getTemperature);
        BigDecimal avgHumidity = calculateAverage(mergedData, AgricultureWeatherData::getHumidity);
        BigDecimal avgWindSpeed = calculateAverage(mergedData, AgricultureWeatherData::getWindSpeed);
        BigDecimal avgLightIntensity = calculateAverage(mergedData, AgricultureWeatherData::getLightIntensity);
        BigDecimal avgRainfall = calculateAverage(mergedData, AgricultureWeatherData::getRainfall);
        BigDecimal avgAirPressure = calculateAverage(mergedData, AgricultureWeatherData::getAirPressure);

        return AgricultureWeatherHourlyStats.builder()
                .pastureId(dataList.get(0).getPastureId())
                .batchId(dataList.get(0).getBatchId())
                .statHour(hour)
                .avgTemperature(avgTemperature)
                .avgHumidity(avgHumidity)
                .avgWindSpeed(avgWindSpeed)
                .avgLightIntensity(avgLightIntensity)
                .avgRainfall(avgRainfall)
                .avgAirPressure(avgAirPressure)
                .dataCount(dataList.size())
                .createTime(LocalDateTime.now())
                .build();
    }

    /**
     * 从原始数据计算日统计数据
     */
    private AgricultureWeatherDailyStats calculateDailyStatsFromRawData(List<AgricultureWeatherData> dataList, LocalDate date) {
        if (dataList.isEmpty()) {
            return null;
        }

        // 合并同采集时间的数据
        List<AgricultureWeatherData> mergedData = mergeSameTimeData(dataList);

        // 计算平均值
        BigDecimal avgTemperature = calculateAverage(mergedData, AgricultureWeatherData::getTemperature);
        BigDecimal avgHumidity = calculateAverage(mergedData, AgricultureWeatherData::getHumidity);
        BigDecimal avgWindSpeed = calculateAverage(mergedData, AgricultureWeatherData::getWindSpeed);
        BigDecimal avgLightIntensity = calculateAverage(mergedData, AgricultureWeatherData::getLightIntensity);
        BigDecimal avgRainfall = calculateAverage(mergedData, AgricultureWeatherData::getRainfall);
        BigDecimal avgAirPressure = calculateAverage(mergedData, AgricultureWeatherData::getAirPressure);

        return AgricultureWeatherDailyStats.builder()
                .pastureId(dataList.get(0).getPastureId())
                .batchId(dataList.get(0).getBatchId())
                .statDate(date)
                .avgTemperature(avgTemperature)
                .avgHumidity(avgHumidity)
                .avgWindSpeed(avgWindSpeed)
                .avgLightIntensity(avgLightIntensity)
                .avgRainfall(avgRainfall)
                .avgAirPressure(avgAirPressure)
                .dataCount(dataList.size())
                .createTime(LocalDateTime.now())
                .build();
    }

    /**
     * 计算平均值（用于统计计算）
     */
    private BigDecimal calculateAverage(List<AgricultureWeatherData> dataList, Function<AgricultureWeatherData, Double> getter) {
        List<Double> validValues = dataList.stream()
                .map(getter)
                .filter(value -> value != null && value != 0)
                .collect(Collectors.toList());

        if (validValues.isEmpty()) {
            return BigDecimal.ZERO;
        }

        double avg = validValues.stream().mapToDouble(Double::doubleValue).average().orElse(0);
        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
    }

    private Double round(Double value, int scale) {
        if (value == null) return null;
        return new BigDecimal(value).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    @Override
    public AgricultureWeatherData getLatestMergedWeatherData(String pastureId) {
        int maxTries = 10; // 最多往前查10批，防止死循环
        LocalDateTime lastTime = null;

        for (int i = 0; i < maxTries; i++) {
            // 1. 查最新一批采集时间
            LambdaQueryWrapper<AgricultureWeatherData> wrapper = new LambdaQueryWrapper<AgricultureWeatherData>()
                    .eq(AgricultureWeatherData::getPastureId, pastureId)
                    .orderByDesc(AgricultureWeatherData::getCollectTime)
                    .select(AgricultureWeatherData::getCollectTime);
            if (lastTime != null) {
                wrapper.lt(AgricultureWeatherData::getCollectTime, lastTime);
            }
            AgricultureWeatherData latest = this.getOne(wrapper.last("limit 1"));
            if (latest == null || latest.getCollectTime() == null) {
                return null;
            }
            lastTime = latest.getCollectTime();

            // 2. 查该时间窗口内所有数据（6秒窗口）
            LocalDateTime windowStart = lastTime.minusSeconds(6);
            List<AgricultureWeatherData> recentList = this.lambdaQuery()
                    .eq(AgricultureWeatherData::getPastureId, pastureId)
                    .ge(AgricultureWeatherData::getCollectTime, windowStart)
                    .le(AgricultureWeatherData::getCollectTime, lastTime)
                    .list();

            if (recentList == null || recentList.isEmpty()) {
                continue;
            }

            // 3. 每个设备取最新一条
            Map<Long, AgricultureWeatherData> deviceLatestMap = new HashMap<>();
            for (AgricultureWeatherData d : recentList) {
                Long deviceId = d.getDeviceId();
                if (!deviceLatestMap.containsKey(deviceId) ||
                        d.getCollectTime().isAfter(deviceLatestMap.get(deviceId).getCollectTime())) {
                    deviceLatestMap.put(deviceId, d);
                }
            }
            List<AgricultureWeatherData> toMerge = new ArrayList<>(deviceLatestMap.values());

            // 4. 合并
            List<AgricultureWeatherData> merged = mergeSameTimeDatatAndWindDirection(toMerge);
            if (!merged.isEmpty()) {
                AgricultureWeatherData data = merged.get(0);
                // 5. 判断是否全为0或null（关键字段）
                boolean allZeroOrNull =
                        (data.getTemperature() == null || data.getTemperature() == 0) &&
                                (data.getHumidity() == null || data.getHumidity() == 0) &&
                                (data.getWindSpeed() == null || data.getWindSpeed() == 0) &&
                                (data.getLightIntensity() == null || data.getLightIntensity() == 0);
                if (!allZeroOrNull) {
                    return data;
                }
            }
            // 否则继续往前找
        }
        // 如果找了10批都没有有效数据，返回null或自定义提示
        return null;
    }
}
