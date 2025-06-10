package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.dto.AgricultureCropBatchDTO;

/**
 * 分区Service接口
 *
 * @author server
 * @date 2025-05-28
 */
public interface AgricultureCropBatchService extends IService<AgricultureCropBatch>
{
    /**
     * 查询分区
     *
     * @param batchId 分区主键
     * @return 分区
     */
    public AgricultureCropBatch selectAgricultureCropBatchByBatchId(Long batchId);

    /**
     * 根据大棚ID查询分区列表
     *
     * @param pastureId 大棚ID
     * @return 分区列表
     */
    List<AgricultureCropBatchDTO> selectBatchByPastureId(Long pastureId);

    /**
     * 导出分区列表
     *
     * @param agricultureCropBatch 分区
     * @return 分区集合
     */
    public List<AgricultureCropBatch> selectAgricultureCropBatchList(AgricultureCropBatch agricultureCropBatch);

    /**
     * 新增分区
     *
     * @param agricultureCropBatch 分区
     * @return 结果
     */
    public int insertAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch);

    /**
     * 修改分区
     *
     * @param agricultureCropBatch 分区
     * @return 结果
     */
    public int updateAgricultureCropBatch(AgricultureCropBatch agricultureCropBatch);

    /**
     * 批量删除分区
     *
     * @param batchId 需要删除的分区主键集合
     * @return 结果
     */
    public int deleteAgricultureCropBatchByBatchIds(Long[] batchId);

    /**
     * 删除分区信息
     *
     * @param batchId 分区主键
     * @return 结果
     */
    public int deleteAgricultureCropBatchByBatchId(Long batchId);

    /**
     * 链表返回
     * @return
     */
    List<AgricultureCropBatchDTO> getCropBatchWithClassImages(AgricultureCropBatchDTO agricultureCropBatchDTO);
}
