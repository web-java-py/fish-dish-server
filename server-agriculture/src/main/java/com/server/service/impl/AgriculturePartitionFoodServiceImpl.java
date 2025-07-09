package com.server.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.*;
import com.server.domain.dto.AgriculturePartitionFoodPageDTO;
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

        // 5. 合并所有批次任务的计划周期，查环境数据（如需更细粒度可循环查）
        List<AgricultureWeatherData> weatherList = new ArrayList<>();
        List<AgricultureWaterQualityData> waterList = new ArrayList<>();
        if (!batchTaskList.isEmpty()) {
            // 取所有批次任务的最早开始和最晚结束
            Date minStart = batchTaskList.stream()
                    .filter(t -> t.getPlanStart() != null)
                    .map(AgricultureBatchTask::getPlanStart)
                    .min(Date::compareTo).orElse(null);
            Date maxFinish = batchTaskList.stream()
                    .filter(t -> t.getPlanFinish() != null)
                    .map(AgricultureBatchTask::getPlanFinish)
                    .max(Date::compareTo).orElse(null);

            if (minStart != null && maxFinish != null) {
                weatherList = weatherDataMapper.selectList(
                        new QueryWrapper<AgricultureWeatherData>()
                                .eq("batch_id", food.getIaPartitionId())
                                .between("collect_time", minStart, maxFinish)
                );
                waterList = waterQualityDataMapper.selectList(
                        new QueryWrapper<AgricultureWaterQualityData>()
                                .eq("batch_id", food.getIaPartitionId())
                                .between("collect_time", minStart, maxFinish)
                );
            }
        }

        // 6. 组装VO
        TraceabilityDetailVO vo = new TraceabilityDetailVO();
        vo.setFoodInfo(food);
        vo.setCropBatch(cropBatch);
        vo.setPastureInfo(pasture);
        vo.setBatchTaskList(batchTaskList);
        vo.setWeatherDataList(weatherList);
        vo.setWaterQualityDataList(waterList);
        return vo;
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
