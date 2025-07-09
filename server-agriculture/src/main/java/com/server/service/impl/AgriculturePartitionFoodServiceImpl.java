package com.server.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
import com.server.domain.vo.BatchTaskDetailVO;
import com.server.domain.vo.TraceabilityDetailVO;
import com.server.mapper.*;
import com.server.utils.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.service.AgriculturePartitionFoodService;

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

    /**
     * 根据溯源码查询溯源详情信息，包括分区、大棚、批次任务、环境数据等
     *
     * @param traceId 溯源码（溯源id）
     * @return TraceabilityDetailVO 溯源详情VO
     * @throws RuntimeException 如果溯源信息不存在
     */
    @Override
    public TraceabilityDetailVO getTraceabilityDetailById(String traceId) {
        // 1. 查溯源表
        AgriculturePartitionFood food = agriculturePartitionFoodMapper.selectById(traceId);
        if (food == null) {
            throw new RuntimeException("溯源信息不存在");
        }

        // 2. 查分区
        AgricultureCropBatch cropBatch = agricultureCropBatchMapper.selectById(food.getIaPartitionId());

        // 3. 查大棚
        AgriculturePasture pasture = null;
        if (cropBatch != null) {
            pasture = pastureMapper.selectById(cropBatch.getPastureId());
        }

        // 4. 查所有批次任务
        List<AgricultureBatchTask> batchTaskList = batchTaskMapper.selectList(
                new QueryWrapper<AgricultureBatchTask>().eq("batch_id", food.getIaPartitionId())
        );

        List<BatchTaskDetailVO> batchTaskDetailList = new ArrayList<>();

        for (AgricultureBatchTask batchTask : batchTaskList) {
            BatchTaskDetailVO detailVO = new BatchTaskDetailVO();
            detailVO.setBatchTask(batchTask);

            // 只处理有实际开始和结束时间的任务
            if (batchTask.getActualStart() != null && batchTask.getActualFinish() != null) {
                // 1. 查询气象原始数据（大棚id+分区id+时间区间）
                List<AgricultureWeatherData> weatherDataList = weatherDataMapper.selectList(
                        new QueryWrapper<AgricultureWeatherData>()
                                .eq("pasture_id", cropBatch.getPastureId())
                                .eq("batch_id", food.getIaPartitionId())
                                .between("collect_time", batchTask.getActualStart(), batchTask.getActualFinish())
                                .orderByAsc("collect_time")
                );
                // 2. 聚合气象数据（每3条合成一条）
                List<AgricultureWeatherData> weatherMergedList = mergeWeatherDataByThree(weatherDataList);

                // 3. 计算气象平均值（对聚合后的数据做平均）
                Map<String, Double> weatherAvg = calculateWeatherAvg(weatherMergedList);

                // 4. 查询水质原始数据
                List<AgricultureWaterQualityData> waterQualityDataList = waterQualityDataMapper.selectList(
                        new QueryWrapper<AgricultureWaterQualityData>()
                                .eq("pasture_id", cropBatch.getPastureId())
                                .eq("batch_id", food.getIaPartitionId())
                                .between("collect_time", batchTask.getActualStart(), batchTask.getActualFinish())
                                .orderByAsc("collect_time")
                );
                // 5. 计算水质平均值（对所有原始数据做平均）
                Map<String, Double> waterQualityAvg = calculateWaterQualityAvg(waterQualityDataList);

                // 设置聚合后的气象数据列表
                // detailVO.setWeatherMergedList(weatherMergedList);

                // 设置气象平均值（基于聚合数据）
                detailVO.setWeatherAvg(weatherAvg);
                // 设置所有水质原始数据
                // detailVO.setWaterQualityDataList(waterQualityDataList); // 所有水质数据

                // 设置水质平均值
                detailVO.setWaterQualityAvg(waterQualityAvg);
            }
            batchTaskDetailList.add(detailVO);
        }

        // 5. 组装VO
        TraceabilityDetailVO vo = new TraceabilityDetailVO();
        vo.setFoodInfo(food);
        vo.setCropBatch(cropBatch);
        vo.setPastureInfo(pasture);
        vo.setBatchTaskDetailList(batchTaskDetailList);
        return vo;
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
                // 直接生成二维码的Base64字符串
            String barcodeBase64 = QRCodeUtil.generateQRCode(bean.getId().toString());
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

}
