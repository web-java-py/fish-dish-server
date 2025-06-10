package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureWeatherData;

import java.util.List;

/**
 * 气象数据Service接口
 * 
 * @author bxwy
 * @date 2025-06-08
 */
public interface AgricultureWeatherDataService extends IService<AgricultureWeatherData>
{
    /**
     * 查询气象数据
     * 
     * @param id 气象数据主键
     * @return 气象数据
     */
    public AgricultureWeatherData selectAgricultureWeatherDataById(Long id);

    /**
     * 查询气象数据列表
     * 
     * @param agricultureWeatherData 气象数据
     * @return 气象数据集合
     */
    public List<AgricultureWeatherData> selectAgricultureWeatherDataList(AgricultureWeatherData agricultureWeatherData);

    /**
     * 新增气象数据
     * 
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    public int insertAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData);

    /**
     * 修改气象数据
     * 
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    public int updateAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData);

    /**
     * 批量删除气象数据
     * 
     * @param ids 需要删除的气象数据主键集合
     * @return 结果
     */
    public int deleteAgricultureWeatherDataByIds(Long[] ids);

    /**
     * 删除气象数据信息
     * 
     * @param id 气象数据主键
     * @return 结果
     */
    public int deleteAgricultureWeatherDataById(Long id);
}
