package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.dto.AgricultureCropBatchDTO;
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
     * 根据大棚ID查询分区列表
     *
     * @param pastureId 大棚ID
     * @return 分区列表
     */
    @Override
    public List<AgricultureCropBatchDTO> selectBatchByPastureId(Long pastureId) {
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AgricultureCropBatch::getPastureId, pastureId);
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }

    /**
     * 导出分区列表
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
     * @param batchId 需要删除的分区主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCropBatchByBatchIds(Long[] batchId)
    {
        return removeByIds(Arrays.asList(batchId)) ? batchId.length : 0;
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

    /**
     * 根据条件查询作物批次信息，并包含相关的分类图片
     *
     * @param agricultureCropBatchDTO 包含查询条件的DTO，如批次名称batchName
     * @return 符合条件的作物批次DTO列表
     */
    @Override
    public List<AgricultureCropBatchDTO> getCropBatchWithClassImages(AgricultureCropBatchDTO agricultureCropBatchDTO) {
        // 创建LambdaQueryWrapper，用于构建查询条件，基于数据库实体类AgricultureCropBatch
        LambdaQueryWrapper<AgricultureCropBatch> queryWrapper = new LambdaQueryWrapper<>();
        // 如果DTO中的批次名称不为空，则添加批次名称的模糊查询条件
        if (StringUtils.isNotEmpty(agricultureCropBatchDTO.getBatchName())) {
            queryWrapper.like(AgricultureCropBatch::getBatchName, agricultureCropBatchDTO.getBatchName());
        }

        // 调用Mapper方法执行查询，并将Wrapper作为参数传递，让Mapper根据条件拼接SQL
        return agricultureCropBatchMapper.selectCropBatchWithClassImages(queryWrapper);
    }
}
