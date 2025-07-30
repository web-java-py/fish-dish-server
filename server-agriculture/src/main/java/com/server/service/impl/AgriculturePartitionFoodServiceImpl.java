package com.server.service.impl;

import java.util.*;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
import com.server.domain.vo.BatchTaskDetailVO;
import com.server.domain.vo.TraceabilityDetailVO;
import com.server.domain.vo.DeviceWithThresholdVO;
import com.server.service.AgricultureWaterQualityHourlyStatsService;
import com.server.service.AgricultureWaterQualityDailyStatsService;
import com.server.mapper.*;
import com.server.service.*;
import com.server.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.server.service.AgriculturePartitionFoodService;
import com.server.service.AgricultureTraceabilityLogService;
import com.server.service.AgricultureDeviceSensorAlertService;

/**
 * 分区食品 采摘Service业务层处理
 * 
 * @author server
 * @date 2025-06-24
 */
@Service
public class AgriculturePartitionFoodServiceImpl extends ServiceImpl<AgriculturePartitionFoodMapper, AgriculturePartitionFood> implements AgriculturePartitionFoodService
{
    // 注入分区食品采摘的Mapper，用于操作分区食品采摘表
    @Autowired
    private AgriculturePartitionFoodMapper agriculturePartitionFoodMapper;

    // 注入分区Mapper，用于操作分区表
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    // 注入大棚Mapper，用于操作大棚表
    @Autowired
    private AgriculturePastureMapper pastureMapper;

    // 注入批次任务Mapper，用于操作批次任务表
    @Autowired
    private AgricultureBatchTaskMapper batchTaskMapper;

    // 注入气象数据Mapper，用于操作气象数据表
    @Autowired
    private AgricultureWeatherDataMapper weatherDataMapper;

    // 注入水质数据Mapper，用于操作水质数据表
    @Autowired
    private AgricultureWaterQualityDataMapper waterQualityDataMapper;

    // 注入小时统计服务
    @Autowired
    private AgricultureWeatherHourlyStatsService hourlyStatsService;

    // 注入日统计服务
    @Autowired
    private AgricultureWeatherDailyStatsService dailyStatsService;

    // 注入水质小时统计服务
    @Autowired
    private AgricultureWaterQualityHourlyStatsService waterQualityHourlyStatsService;

    // 注入水质日统计服务
    @Autowired
    private AgricultureWaterQualityDailyStatsService waterQualityDailyStatsService;

    // 注入溯源查询记录服务
    @Autowired
    private AgricultureTraceabilityLogService traceabilityLogService;

    @Autowired
    private AgricultureDeviceService agricultureDeviceService;
    @Autowired
    private AgricultureThresholdConfigService agricultureThresholdConfigService;
    @Autowired
    private AgricultureDeviceSensorAlertService sensorAlertService;

    @Value("${codepath}")
    private String codepath;

    /**
     * 根据溯源码查询溯源详情信息，包括分区、大棚、批次任务、环境数据等
     *
     * @param traceId 溯源码（溯源id）
     * @param queryIp 查询IP
     * @param userAgent 用户代理
     * @param queryType 查询类型
     * @return TraceabilityDetailVO 溯源详情VO
     * @throws RuntimeException 如果溯源信息不存在
     */
    @Override
    public TraceabilityDetailVO getTraceabilityDetailById(String traceId, String queryIp, String userAgent, String queryType, Date firstTraceTime) {
        // 1. 查溯源表
        AgriculturePartitionFood food = agriculturePartitionFoodMapper.selectById(traceId);
        if (food == null) {
            throw new RuntimeException("溯源信息不存在");
        }

        // 2. 首次溯源时间处理
        if (food.getFirstTraceTime() == null && firstTraceTime != null) {
            food.setFirstTraceTime(firstTraceTime);
            agriculturePartitionFoodMapper.updateById(food);
        }

        // 3. 记录查询日志
        traceabilityLogService.recordTraceabilityQuery(traceId, food.getIaPartitionId(), queryIp, userAgent, queryType,food.getFoodType());

        // 4. 查询该溯源码的溯源次数
        Long traceCount = traceabilityLogService.getTraceabilityCountByCode(traceId);

        // 5. 查分区
        AgricultureCropBatch cropBatch = agricultureCropBatchMapper.selectById(food.getIaPartitionId());

        // 6. 格式化分区的创建时间为年-月-日格式
        String formattedCreateTime = null;
        if (cropBatch != null && cropBatch.getCreateTime() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            formattedCreateTime = dateFormat.format(cropBatch.getCreateTime());
        }

        // 7. 查大棚
        AgriculturePasture pasture = null;
        if (cropBatch != null) {
            pasture = pastureMapper.selectById(cropBatch.getPastureId());
        }

        // 8. 查所有批次任务
        List<AgricultureBatchTask> batchTaskList = batchTaskMapper.selectList(
            new QueryWrapper<AgricultureBatchTask>().eq("batch_id", food.getIaPartitionId())
        );
        List<BatchTaskDetailVO> batchTaskDetailList = new ArrayList<>();
        for (AgricultureBatchTask batchTask : batchTaskList) {
            BatchTaskDetailVO detailVO = new BatchTaskDetailVO();
            detailVO.setBatchTask(batchTask);

            // 只处理有实际开始和结束时间的任务
            if (batchTask.getActualStart() != null && batchTask.getActualFinish() != null) {
                // 优先使用聚合表查询气象数据
                Map<String, Double> weatherAvg = getWeatherAvgFromStats(cropBatch.getPastureId().toString(), food.getIaPartitionId(), batchTask.getActualStart(), batchTask.getActualFinish());

                // 优先使用聚合表查询水质数据
                Map<String, Double> waterQualityAvg = getWaterQualityAvgFromStats(cropBatch.getPastureId().toString(), food.getIaPartitionId(), batchTask.getActualStart(), batchTask.getActualFinish());

                // 设置气象平均值，保留两位小数
                detailVO.setWeatherAvg(formatDecimalMap(weatherAvg));
                // 设置水质平均值，保留两位小数
                detailVO.setWaterQualityAvg(formatDecimalMap(waterQualityAvg));
            }
            batchTaskDetailList.add(detailVO);
        }

        // 9. 查设备及阈值配置（只返回有阈值配置的所有阈值配置信息）
        List<Long> deviceIds = agricultureDeviceService.selectDeviceIdsByPastureAndBatch(
            cropBatch != null ? Long.valueOf(cropBatch.getPastureId()) : null,
            cropBatch != null ? Long.valueOf(cropBatch.getBatchId()) : null
        );
        List<AgricultureThresholdConfig> thresholdConfigList = new ArrayList<>();
        if (deviceIds != null && !deviceIds.isEmpty()) {
            thresholdConfigList = agricultureThresholdConfigService.selectByDeviceIds(deviceIds);
        }

        // 10. 查大棚和分区下的所有预警信息
        List<AgricultureDeviceSensorAlert> allSensorAlerts = sensorAlertService.list(
            new QueryWrapper<AgricultureDeviceSensorAlert>()
                .eq("pasture_id", cropBatch.getPastureId())
                .eq("batch_id", cropBatch.getBatchId())
        );

        // 11. 归属到批次任务区间
        for (BatchTaskDetailVO detailVO : batchTaskDetailList) {
            AgricultureBatchTask batchTask = detailVO.getBatchTask();
            if (batchTask.getActualStart() != null && batchTask.getActualFinish() != null) {
                LocalDateTime start = toLocalDateTime(batchTask.getActualStart());
                LocalDateTime end = toLocalDateTime(batchTask.getActualFinish());
                
                // 如果开始时间和结束时间在同一天，将结束时间调整为当天的23:59:59
                if (start != null && end != null && start.toLocalDate().equals(end.toLocalDate())) {
                    end = end.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
                }
                
                // 新建 final 局部变量
                final LocalDateTime realStart, realEnd;
                if (start != null && end != null && start.isAfter(end)) {
                    realStart = end;
                    realEnd = start;
                } else {
                    realStart = start;
                    realEnd = end;
                }
                List<AgricultureDeviceSensorAlert> alertsInTask = allSensorAlerts.stream()
                    .filter(alert -> {
                        LocalDateTime alertTime = alert.getAlertTime();
                        return alertTime != null &&
                            !alertTime.isBefore(realStart) &&
                            !alertTime.isAfter(realEnd);
                    })
                    .collect(Collectors.toList());
                detailVO.setSensorAlertList(alertsInTask);
                detailVO.setAlertCount(alertsInTask.size());
            } else {
                detailVO.setSensorAlertList(Collections.emptyList());
                detailVO.setAlertCount(0);
            }
        }

        // 12. 组装VO
        TraceabilityDetailVO vo = new TraceabilityDetailVO();
        vo.setFoodInfo(food);
        vo.setCropBatch(cropBatch);
        vo.setPastureInfo(pasture);
        vo.setBatchTaskDetailList(batchTaskDetailList);
        vo.setCropBatchCreateTimeFormatted(formattedCreateTime);
        vo.setTraceCount(traceCount);
        vo.setThresholdConfigList(thresholdConfigList);
        vo.setSensorAlertList(allSensorAlerts);
        return vo;
    }

    /**
     * 根据溯源码查询溯源详情信息（重载方法，不记录日志）
     *
     * @param traceId 溯源码（溯源id）
     * @return TraceabilityDetailVO 溯源详情VO
     * @throws RuntimeException 如果溯源信息不存在
     */
    @Override
    public TraceabilityDetailVO getTraceabilityDetailById(String traceId) {
        // 调用带日志记录的方法，传入默认值
        return getTraceabilityDetailById(traceId, null, null, null,null);
    }

    /**
     * 优先从聚合表获取气象平均值，如果没有数据则从原始数据计算
     */
    private Map<String, Double> getWeatherAvgFromStats(String pastureId, String batchId, Date startTime, Date endTime) {
        // 转换Date为LocalDateTime
        LocalDateTime start = startTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        
        // 如果开始时间和结束时间在同一天，将结束时间调整为当天的23:59:59
        if (start.toLocalDate().equals(end.toLocalDate())) {
            end = end.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        }
        
        // 计算时间跨度
        long hoursDiff = ChronoUnit.HOURS.between(start, end);
        
        Map<String, Double> weatherAvg = new HashMap<>();
        
        if (hoursDiff <= 24) {
            // 24小时内，优先使用小时统计表
            List<AgricultureWeatherHourlyStats> hourlyStats = hourlyStatsService.getHourlyStats(pastureId, batchId, start, end);
            if (!hourlyStats.isEmpty()) {
                weatherAvg = calculateAvgFromHourlyStats(hourlyStats);
            }
        } else {
            // 超过24小时，优先使用日统计表
            List<AgricultureWeatherDailyStats> dailyStats = dailyStatsService.getDailyStats(pastureId, batchId, start, end);
            if (!dailyStats.isEmpty()) {
                weatherAvg = calculateAvgFromDailyStats(dailyStats);
            }
        }
        
        // 如果聚合表没有数据，则查询原始数据
        if (weatherAvg.isEmpty()) {
            // 将调整后的LocalDateTime转换回Date用于查询
            Date adjustedStartTime = Date.from(start.atZone(java.time.ZoneId.systemDefault()).toInstant());
            Date adjustedEndTime = Date.from(end.atZone(java.time.ZoneId.systemDefault()).toInstant());
            
            List<AgricultureWeatherData> weatherDataList = weatherDataMapper.selectList(
                    new QueryWrapper<AgricultureWeatherData>()
                            .eq("pasture_id", pastureId)
                            .eq("batch_id", batchId)
                            .between("collect_time", adjustedStartTime, adjustedEndTime)
                            .orderByAsc("collect_time")
            );
            
            // 聚合气象数据（每3条合成一条）
            List<AgricultureWeatherData> weatherMergedList = mergeWeatherDataByThree(weatherDataList);
            // 计算气象平均值（对聚合后的数据做平均）
            weatherAvg = calculateWeatherAvg(weatherMergedList);
        }
        
        return weatherAvg;
    }

    /**
     * 优先从聚合表获取水质平均值，如果没有数据则从原始数据计算
     */
    private Map<String, Double> getWaterQualityAvgFromStats(String pastureId, String batchId, Date startTime, Date endTime) {
        // 转换Date为LocalDateTime
        LocalDateTime start = startTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = endTime.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
        
        // 如果开始时间和结束时间在同一天，将结束时间调整为当天的23:59:59
        if (start.toLocalDate().equals(end.toLocalDate())) {
            end = end.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
        }
        
        // 计算时间跨度
        long hoursDiff = ChronoUnit.HOURS.between(start, end);
        
        Map<String, Double> waterQualityAvg = new HashMap<>();
        
        if (hoursDiff <= 24) {
            // 24小时内，优先使用小时统计表
            List<AgricultureWaterQualityHourlyStats> hourlyStats = waterQualityHourlyStatsService.getHourlyStats(pastureId, batchId, start, end);
            if (!hourlyStats.isEmpty()) {
                waterQualityAvg = calculateAvgFromWaterQualityHourlyStats(hourlyStats);
            }
        } else {
            // 超过24小时，优先使用日统计表
            List<AgricultureWaterQualityDailyStats> dailyStats = waterQualityDailyStatsService.getDailyStats(pastureId, batchId, start, end);
            if (!dailyStats.isEmpty()) {
                waterQualityAvg = calculateAvgFromWaterQualityDailyStats(dailyStats);
            }
        }
        
        // 如果聚合表没有数据，则查询原始数据
        if (waterQualityAvg.isEmpty()) {
            // 将调整后的LocalDateTime转换回Date用于查询
            Date adjustedStartTime = Date.from(start.atZone(java.time.ZoneId.systemDefault()).toInstant());
            Date adjustedEndTime = Date.from(end.atZone(java.time.ZoneId.systemDefault()).toInstant());
            
            List<AgricultureWaterQualityData> waterQualityDataList = waterQualityDataMapper.selectList(
                    new QueryWrapper<AgricultureWaterQualityData>()
                            .eq("pasture_id", pastureId)
                            .eq("batch_id", batchId)
                            .between("collect_time", adjustedStartTime, adjustedEndTime)
                            .orderByAsc("collect_time")
            );
            
            // 计算水质平均值（对原始数据做平均）
            waterQualityAvg = calculateWaterQualityAvg(waterQualityDataList);
        }
        
        return waterQualityAvg;
    }

    /**
     * 从小时统计数据计算平均值
     */
    private Map<String, Double> calculateAvgFromHourlyStats(List<AgricultureWeatherHourlyStats> stats) {
        Map<String, Double> avg = new HashMap<>();
        
        // 过滤掉全为0的无效数据
        List<AgricultureWeatherHourlyStats> validStats = stats.stream()
                .filter(stat -> {
                    boolean allZero = (stat.getAvgTemperature() == null || stat.getAvgTemperature().doubleValue() == 0)
                            && (stat.getAvgHumidity() == null || stat.getAvgHumidity().doubleValue() == 0)
                            && (stat.getAvgWindSpeed() == null || stat.getAvgWindSpeed().doubleValue() == 0)
                            && (stat.getAvgLightIntensity() == null || stat.getAvgLightIntensity().doubleValue() == 0);
                    return !allZero;
                })
                .collect(Collectors.toList());
        
        if (validStats.isEmpty()) {
            avg.put("temperature", 0.0);
            avg.put("humidity", 0.0);
            avg.put("windSpeed", 0.0);
            avg.put("lightIntensity", 0.0);
            return avg;
        }
        
        // 计算平均值
        avg.put("temperature", validStats.stream()
                .map(AgricultureWeatherHourlyStats::getAvgTemperature)
                .filter(val -> val != null && val.doubleValue() != 0)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("humidity", validStats.stream()
                .map(AgricultureWeatherHourlyStats::getAvgHumidity)
                .filter(val -> val != null && val.doubleValue() != 0)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("windSpeed", validStats.stream()
                .map(AgricultureWeatherHourlyStats::getAvgWindSpeed)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("lightIntensity", validStats.stream()
                .map(AgricultureWeatherHourlyStats::getAvgLightIntensity)
                .filter(val -> val != null && val.doubleValue() != 0)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        return avg;
    }

    /**
     * 从日统计数据计算平均值
     */
    private Map<String, Double> calculateAvgFromDailyStats(List<AgricultureWeatherDailyStats> stats) {
        Map<String, Double> avg = new HashMap<>();
        
        // 过滤掉全为0的无效数据
        List<AgricultureWeatherDailyStats> validStats = stats.stream()
                .filter(stat -> {
                    boolean allZero = (stat.getAvgTemperature() == null || stat.getAvgTemperature().doubleValue() == 0)
                            && (stat.getAvgHumidity() == null || stat.getAvgHumidity().doubleValue() == 0)
                            && (stat.getAvgWindSpeed() == null || stat.getAvgWindSpeed().doubleValue() == 0)
                            && (stat.getAvgLightIntensity() == null || stat.getAvgLightIntensity().doubleValue() == 0);
                    return !allZero;
                })
                .collect(Collectors.toList());
        
        if (validStats.isEmpty()) {
            avg.put("temperature", 0.0);
            avg.put("humidity", 0.0);
            avg.put("windSpeed", 0.0);
            avg.put("lightIntensity", 0.0);
            return avg;
        }
        
        // 计算平均值
        avg.put("temperature", validStats.stream()
                .map(AgricultureWeatherDailyStats::getAvgTemperature)
                .filter(val -> val != null && val.doubleValue() != 0)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("humidity", validStats.stream()
                .map(AgricultureWeatherDailyStats::getAvgHumidity)
                .filter(val -> val != null && val.doubleValue() != 0)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("windSpeed", validStats.stream()
                .map(AgricultureWeatherDailyStats::getAvgWindSpeed)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("lightIntensity", validStats.stream()
                .map(AgricultureWeatherDailyStats::getAvgLightIntensity)
                .filter(val -> val != null && val.doubleValue() != 0)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        return avg;
    }

    /**
     * 从水质小时统计数据计算平均值
     */
    private Map<String, Double> calculateAvgFromWaterQualityHourlyStats(List<AgricultureWaterQualityHourlyStats> stats) {
        Map<String, Double> avg = new HashMap<>();
        
        // 过滤掉全为0的无效数据
        List<AgricultureWaterQualityHourlyStats> validStats = stats.stream()
                .filter(stat -> {
                    boolean allZero = (stat.getAvgPhValue() == null || stat.getAvgPhValue().doubleValue() == 0)
                            && (stat.getAvgDissolvedOxygen() == null || stat.getAvgDissolvedOxygen().doubleValue() == 0)
                            && (stat.getAvgAmmoniaNitrogen() == null || stat.getAvgAmmoniaNitrogen().doubleValue() == 0)
                            && (stat.getAvgWaterTemperature() == null || stat.getAvgWaterTemperature().doubleValue() == 0)
                            && (stat.getAvgConductivity() == null || stat.getAvgConductivity().doubleValue() == 0);
                    return !allZero;
                })
                .collect(Collectors.toList());
        
        if (validStats.isEmpty()) {
            avg.put("phValue", 0.0);
            avg.put("dissolvedOxygen", 0.0);
            avg.put("ammoniaNitrogen", 0.0);
            avg.put("waterTemperature", 0.0);
            avg.put("conductivity", 0.0);
            return avg;
        }
        
        // 计算平均值
        avg.put("phValue", validStats.stream()
                .map(AgricultureWaterQualityHourlyStats::getAvgPhValue)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("dissolvedOxygen", validStats.stream()
                .map(AgricultureWaterQualityHourlyStats::getAvgDissolvedOxygen)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("ammoniaNitrogen", validStats.stream()
                .map(AgricultureWaterQualityHourlyStats::getAvgAmmoniaNitrogen)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("waterTemperature", validStats.stream()
                .map(AgricultureWaterQualityHourlyStats::getAvgWaterTemperature)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("conductivity", validStats.stream()
                .map(AgricultureWaterQualityHourlyStats::getAvgConductivity)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        return avg;
    }

    /**
     * 从水质日统计数据计算平均值
     */
    private Map<String, Double> calculateAvgFromWaterQualityDailyStats(List<AgricultureWaterQualityDailyStats> stats) {
        Map<String, Double> avg = new HashMap<>();
        
        // 过滤掉全为0的无效数据
        List<AgricultureWaterQualityDailyStats> validStats = stats.stream()
                .filter(stat -> {
                    boolean allZero = (stat.getAvgPhValue() == null || stat.getAvgPhValue().doubleValue() == 0)
                            && (stat.getAvgDissolvedOxygen() == null || stat.getAvgDissolvedOxygen().doubleValue() == 0)
                            && (stat.getAvgAmmoniaNitrogen() == null || stat.getAvgAmmoniaNitrogen().doubleValue() == 0)
                            && (stat.getAvgWaterTemperature() == null || stat.getAvgWaterTemperature().doubleValue() == 0)
                            && (stat.getAvgConductivity() == null || stat.getAvgConductivity().doubleValue() == 0);
                    return !allZero;
                })
                .collect(Collectors.toList());
        
        if (validStats.isEmpty()) {
            avg.put("phValue", 0.0);
            avg.put("dissolvedOxygen", 0.0);
            avg.put("ammoniaNitrogen", 0.0);
            avg.put("waterTemperature", 0.0);
            avg.put("conductivity", 0.0);
            return avg;
        }
        
        // 计算平均值
        avg.put("phValue", validStats.stream()
                .map(AgricultureWaterQualityDailyStats::getAvgPhValue)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("dissolvedOxygen", validStats.stream()
                .map(AgricultureWaterQualityDailyStats::getAvgDissolvedOxygen)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("ammoniaNitrogen", validStats.stream()
                .map(AgricultureWaterQualityDailyStats::getAvgAmmoniaNitrogen)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("waterTemperature", validStats.stream()
                .map(AgricultureWaterQualityDailyStats::getAvgWaterTemperature)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        avg.put("conductivity", validStats.stream()
                .map(AgricultureWaterQualityDailyStats::getAvgConductivity)
                .filter(Objects::nonNull)
                .mapToDouble(val -> val.doubleValue())
                .average().orElse(0));
        
        return avg;
    }

    /**
     * 格式化Map中的Double值，保留两位小数
     * @param originalMap 原始Map
     * @return 格式化后的Map
     */
    private Map<String, Double> formatDecimalMap(Map<String, Double> originalMap) {
        Map<String, Double> formattedMap = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.##");
        
        for (Map.Entry<String, Double> entry : originalMap.entrySet()) {
            Double value = entry.getValue();
            if (value != null) {
                // 使用DecimalFormat格式化，然后转回Double
                String formatted = df.format(value);
                formattedMap.put(entry.getKey(), Double.parseDouble(formatted));
            } else {
                formattedMap.put(entry.getKey(), 0.0);
            }
        }
        return formattedMap;
    }

        //气象数据聚合
        private List<AgricultureWeatherData> mergeWeatherDataByThree(List<AgricultureWeatherData> dataList) {
            // 将原始气象数据每3条聚合成一条，聚合时取各字段的平均值
            List<AgricultureWeatherData> merged = new ArrayList<>();
            for (int i = 0; i < dataList.size(); i += 3) {
                List<AgricultureWeatherData> group = dataList.subList(i, Math.min(i + 3, dataList.size()));
                AgricultureWeatherData m = new AgricultureWeatherData();
                m.setCollectTime(group.get(0).getCollectTime());
                m.setPastureId(group.get(0).getPastureId());
                m.setBatchId(group.get(0).getBatchId());
                // 计算每组的平均值，温度、湿度、光照强度过滤掉0和null，风速只过滤null
                m.setTemperature(group.stream().map(AgricultureWeatherData::getTemperature).filter(val -> val != null && val != 0).mapToDouble(Double::doubleValue).average().orElse(0));
                m.setHumidity(group.stream().map(AgricultureWeatherData::getHumidity).filter(val -> val != null && val != 0).mapToDouble(Double::doubleValue).average().orElse(0));
                m.setWindSpeed(group.stream().map(AgricultureWeatherData::getWindSpeed).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
                m.setLightIntensity(group.stream().map(AgricultureWeatherData::getLightIntensity).filter(val -> val != null && val != 0).mapToDouble(Double::doubleValue).average().orElse(0));
                // 判断该聚合结果是否全为0或null，如果是则不加入merged
                boolean allFieldsZeroOrNull =
                    (m.getTemperature() == null || m.getTemperature() == 0)
                    && (m.getHumidity() == null || m.getHumidity() == 0)
                    && (m.getWindSpeed() == null || m.getWindSpeed() == 0)
                    && (m.getLightIntensity() == null || m.getLightIntensity() == 0);
                if (!allFieldsZeroOrNull) {
                    merged.add(m);
                }
            }
            return merged;
        }
        //气象平均值
        private Map<String, Double> calculateWeatherAvg(List<AgricultureWeatherData> dataList) {
            // 计算传入气象数据列表的各项平均值，温度、湿度、光照强度过滤掉0和null，风速只过滤null
            // 并且全为0或null的聚合结果不参与平均
            Map<String, Double> avg = new HashMap<>();
            // 过滤掉全为0或null的无效数据
            List<AgricultureWeatherData> filteredList = dataList.stream().filter(data -> {
                Double temperature = data.getTemperature();
                Double humidity = data.getHumidity();
                Double windSpeed = data.getWindSpeed();
                Double lightIntensity = data.getLightIntensity();
                boolean allFieldsZeroOrNull =
                    (temperature == null || temperature == 0)
                    && (humidity == null || humidity == 0)
                    && (windSpeed == null || windSpeed == 0)
                    && (lightIntensity == null || lightIntensity == 0);
                return !allFieldsZeroOrNull;
            }).collect(Collectors.toList());
            avg.put("temperature", filteredList.stream().map(AgricultureWeatherData::getTemperature).filter(val -> val != null && val != 0).mapToDouble(Double::doubleValue).average().orElse(0));
            avg.put("humidity", filteredList.stream().map(AgricultureWeatherData::getHumidity).filter(val -> val != null && val != 0).mapToDouble(Double::doubleValue).average().orElse(0));
            avg.put("windSpeed", filteredList.stream().map(AgricultureWeatherData::getWindSpeed).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
            avg.put("lightIntensity", filteredList.stream().map(AgricultureWeatherData::getLightIntensity).filter(val -> val != null && val != 0).mapToDouble(Double::doubleValue).average().orElse(0));
            return avg;
        }
    //水质平均值
    private Map<String, Double> calculateWaterQualityAvg(List<AgricultureWaterQualityData> dataList) {
        // 计算传入水质数据列表的各项平均值
        Map<String, Double> avg = new HashMap<>();
        avg.put("phValue", dataList.stream().map(AgricultureWaterQualityData::getPhValue).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
        avg.put("dissolvedOxygen", dataList.stream().map(AgricultureWaterQualityData::getDissolvedOxygen).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
        avg.put("ammoniaNitrogen", dataList.stream().map(AgricultureWaterQualityData::getAmmoniaNitrogen).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
        avg.put("waterTemperature", dataList.stream().map(AgricultureWaterQualityData::getWaterTemperature).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
        avg.put("conductivity", dataList.stream().map(AgricultureWaterQualityData::getConductivity).filter(Objects::nonNull).mapToDouble(Double::doubleValue).average().orElse(0));
        return avg;
    }
    /**
     * 查询分区食品 采摘
     * 
     * @param id 分区食品 采摘主键
     * @return 分区食品 采摘
     */
    @Override
    public AgriculturePartitionFood selectagriculturePartitionFoodById(String id)
    {
        return agriculturePartitionFoodMapper.selectById(id);
    }

    /**
     * 查询分区食品 采摘列表
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 分区食品 采摘
     */
    @Override
    public List<AgriculturePartitionFood> selectagriculturePartitionFoodList(AgriculturePartitionFood agriculturePartitionFood)
    {
        LambdaQueryWrapper<AgriculturePartitionFood> lambdaQueryWrapper = new QueryWrapper<AgriculturePartitionFood>().lambda();
        return agriculturePartitionFoodMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增分区食品 采摘
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 结果
     */
    @Override
    public int insertagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood)
    {
        return agriculturePartitionFoodMapper.insert(agriculturePartitionFood);
    }

    /**
     * 修改分区食品 采摘
     * 
     * @param agriculturePartitionFood 分区食品 采摘
     * @return 结果
     */
    @Override
    public int updateagriculturePartitionFood(AgriculturePartitionFood agriculturePartitionFood)
    {
        return agriculturePartitionFoodMapper.updateById(agriculturePartitionFood);
    }

    /**
     * 批量删除分区食品 采摘
     * 
     * @param ids 需要删除的分区食品 采摘主键
     * @return 结果
     */
    @Override
    public int deleteagriculturePartitionFoodByIds(String[] ids)
    {
        return agriculturePartitionFoodMapper.deleteById(ids);
    }

    /**
     * 删除分区食品 采摘信息
     * 
     * @param id 分区食品 采摘主键
     * @return 结果
     */
    @Override
    public int deleteagriculturePartitionFoodById(String id)
    {
        return agriculturePartitionFoodMapper.deleteById(id);
    }

    @Override
    public List<AgriculturePartitionFood> fy(AgriculturePartitionFoodPageDTO baseDTO) {
        QueryWrapper wrapper = new QueryWrapper<Map<String, Object>>();
        wrapper.eq("ia_partition_id",baseDTO.getPartitionId());
        List<AgriculturePartitionFood> agriculturePartitionFoods = agriculturePartitionFoodMapper.selectList(wrapper);
        //生成前端访问页面的条形二维码
        agriculturePartitionFoods.forEach(bean -> {
            try {
                // 生成二维码内容为溯源码跳转地址
                String codeUrl = codepath + bean.getId();
                String barcodeBase64 = QRCodeUtil.generateQRCode(codeUrl);
                // 对应字段设置Base64字符串
                bean.setBarcode(barcodeBase64);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return agriculturePartitionFoods;
    }

    /**
     * 新增采摘 生成二维码展示
     */
//    @Override
//    public List<AgriculturePartitionFood> XzList(AgriculturePartitionFood agriculturePartitionFood) {
//        LambdaQueryWrapper<AgriculturePartitionFood> lambdaQueryWrapper = new QueryWrapper<AgriculturePartitionFood>().lambda();
//        lambdaQueryWrapper.eq(agriculturePartitionFood.getIaPartitionId() != null,
//                AgriculturePartitionFood::getIaPartitionId,
//                agriculturePartitionFood.getIaPartitionId());
//        List<AgriculturePartitionFood> agriculturePartitionFoods = agriculturePartitionFoodMapper.selectList(lambdaQueryWrapper);
//        //生成前端访问页面的条形二维码
//        agriculturePartitionFoods.forEach(bean -> {
//            try {
//                // 直接生成二维码的Base64字符串
//            String barcodeBase64 = QRCodeUtil.generateQRCode(bean.getId().toString());
//                // 对应字段设置Base64字符串
//                bean.setBarcode(barcodeBase64);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return agriculturePartitionFoods;
//    }

    private LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
    }
}
