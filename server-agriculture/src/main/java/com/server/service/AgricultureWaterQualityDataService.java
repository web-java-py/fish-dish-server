package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureWaterQualityData;

import java.util.List;

/**
 * 水质数据Service接口
 * 
 * @author bxwy
 * @date 2025-06-08
 */
public interface AgricultureWaterQualityDataService  extends IService<AgricultureWaterQualityData>
{
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
}
