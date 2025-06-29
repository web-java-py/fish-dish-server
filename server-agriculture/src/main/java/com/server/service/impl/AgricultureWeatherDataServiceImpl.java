package com.server.service.impl;

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
    public Map<String, Object> getTrendData(String pastureId,String batchId, String range) {
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

        // 3. 分桶聚合
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
        for (AgricultureWeatherData d : allData) {
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

        // 4. 计算每个桶的平均值
        List<String> xAxis = new ArrayList<>(bucketMap.keySet());
        List<Double> temperature = new ArrayList<>();
        List<Double> humidity = new ArrayList<>();
        List<Double> windSpeed = new ArrayList<>();
        List<Double> windDirection = new ArrayList<>();
        List<Double> lightIntensity = new ArrayList<>();

        for (List<AgricultureWeatherData> bucket : bucketMap.values()) {
            temperature.add(avg(bucket, AgricultureWeatherData::getTemperature));
            humidity.add(avg(bucket, AgricultureWeatherData::getHumidity));
            windSpeed.add(avgWindSpeed(bucket)); // 用风速专用方法
            lightIntensity.add(avg(bucket, AgricultureWeatherData::getLightIntensity));
        }

        Map<String, Object> result = new HashMap<>();
        result.put("xAxis", xAxis);
        result.put("temperature", temperature);
        result.put("humidity", humidity);
        result.put("windSpeed", windSpeed);
        result.put("windDirection", windDirection);
        result.put("lightIntensity", lightIntensity);
        return result;
    }

    private Double avg(List<AgricultureWeatherData> list, Function<AgricultureWeatherData, ?> getter) {
        if (list == null || list.isEmpty()) return null;
        List<Double> valid = list.stream()
                .map(getter)
                .filter(Objects::nonNull)
                .map(obj -> {
                    if (obj instanceof Number) {
                        double v = ((Number) obj).doubleValue();
                        // 过滤掉0
                        return v == 0.0 ? null : v;
                    }
                    if (obj instanceof String) {
                        String s = ((String) obj).trim();
                        if (s.isEmpty()) return null;
                        try {
                            double v = Double.parseDouble(s);
                            return v == 0.0 ? null : v;
                        } catch (Exception e) {
                            return null;
                        }
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (valid.isEmpty()) return null;
        return valid.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }

    private Double avgWindSpeed(List<AgricultureWeatherData> list) {
        if (list == null || list.isEmpty()) return null;
        List<Double> valid = list.stream()
                .filter(d -> {
                    // 只要风向、温度、湿度都没有值，才是风速数据
                    boolean isWindSpeedData =
                            (d.getWindDirection() == null || d.getWindDirection().trim().isEmpty()) &&
                                    (d.getTemperature() == null || d.getTemperature().doubleValue() == 0.0) &&
                                    (d.getHumidity() == null || d.getHumidity().doubleValue() == 0.0);
                    return isWindSpeedData;
                })
                .map(AgricultureWeatherData::getWindSpeed)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (valid.isEmpty()) return null;
        return valid.stream().mapToDouble(Double::doubleValue).average().orElse(0);
    }
}
