package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCropBatchDTO;
import com.server.domain.AgricultureDeviceSensorAlert;
import com.server.mapper.AgricultureDeviceSensorAlertMapper;
import com.server.service.AgricultureDeviceSensorAlertService;
import com.server.utils.DateUtils;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.domain.AgricultureCropBatch;
import com.server.service.AgricultureCropBatchService;

/**
 * 分区Service业务层处理
 * 
 * @author server
 * @date 2025-05-28
 */
@Service
public class AgricultureCropBatchServiceImpl extends ServiceImpl<AgricultureCropBatchMapper, AgricultureCropBatch> implements AgricultureCropBatchService
{
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    /**
     * 查询分区
     * 
     * @param batchId 分区主键
     * @return 分区
     */
    @Override
    public AgricultureCropBatch selectAgricultureCropBatchByBatchId(Long batchId)
    {
        return getById(batchId);
    }

    /**
     * 查询分区列表
     * 
     * @param agricultureCropBatch 分区
     * @return 分区
     */
    @Override
    public List<AgricultureCropBatch> selectAgricultureCropBatchList(AgricultureCropBatch agricultureCropBatch)
    {
        return list();
    }

    /**
     * 新增分区
     * 
     * @param agricultureCropBatch 分区
     * @return 结果
     */
    @Override
    public int insertAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        agricultureCropBatch.setCreateTime(LocalDateTime.now());
        return agricultureCropBatchMapper.insert(agricultureCropBatch);
    }

    /**
     * 修改分区
     * 
     * @param agricultureCropBatch 分区
     * @return 结果
     */
    @Override
    public int updateAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch)
    {
        agricultureCropBatch.setUpdateTime(LocalDateTime.now());
        return agricultureCropBatchMapper.updateById(agricultureCropBatch);
    }

    /**
     * 批量删除分区
     * 
     * @param batchIds 需要删除的分区主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCropBatchByBatchIds(Long[] batchIds)
    {
        return removeByIds(Arrays.asList(batchIds)) ? batchIds.length : 0;
    }

    /**
     * 删除分区信息
     * 
     * @param batchId 分区主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCropBatchByBatchId(Long batchId)
    {
        return removeById(batchId) ? 1 : 0;
    }


    @Override
    public List<AgricultureCropBatchDTO> getCropBatchWithClassImages() {
        return agricultureCropBatchMapper.selectCropBatchWithClassImages();
    }
}
