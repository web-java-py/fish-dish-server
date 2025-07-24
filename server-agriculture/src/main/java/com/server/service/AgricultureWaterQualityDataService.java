package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureWaterQualityData;

import java.util.List;
import java.util.Map;

/**
 * 水质数据Service接口
 * 
 * @author bxwy
 * @date 2025-06-08
 */
public interface AgricultureWaterQualityDataService  extends IService<AgricultureWaterQualityData>
{



    /**
     * 查询水质趋势数据（支持24小时、7天、30天）
     *
     * @param pastureId
     * @param batchId
     * @param range    时间范围类型，可选值："day"（24小时）、"week"（7天）、"month"（30天）
     * @return         包含x轴时间点和各气象参数趋势的Map数据
     */
    Map<String, Object> getTrendData(String pastureId, String batchId, String range);

    /**
     * 查询水质数据
     * 
     * @param id 水质数据主键
     * @return 水质数据
     */
    public AgricultureWaterQualityData selectAgricultureWaterQualityDataById(Long id);

    /**
     * 查询水质数据列表
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 水质数据集合
     */
    public List<AgricultureWaterQualityData> selectAgricultureWaterQualityDataList(AgricultureWaterQualityData agricultureWaterQualityData);

    /**
     * 新增水质数据
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 结果
     */
    public int insertAgricultureWaterQualityData(AgricultureWaterQualityData agricultureWaterQualityData);

    /**
     * 修改水质数据
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 结果
     */
    public int updateAgricultureWaterQualityData(AgricultureWaterQualityData agricultureWaterQualityData);

    /**
     * 批量删除水质数据
     * 
     * @param ids 需要删除的水质数据主键集合
     * @return 结果
     */
    public int deleteAgricultureWaterQualityDataByIds(Long[] ids);

    /**
     * 删除水质数据信息
     * 
     * @param id 水质数据主键
     * @return 结果
     */
    public int deleteAgricultureWaterQualityDataById(Long id);

    /**
     * 获取最新一条水质数据
     * @param pastureId 大棚ID
     * @return 最新水质数据
     */
    AgricultureWaterQualityData getLatestWaterQualityData(String pastureId);
}
