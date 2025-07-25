package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureWeatherData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 气象数据Service接口
 * 
 * @author bxwy
 * @date 2025-06-08
 */
public interface AgricultureWeatherDataService extends IService<AgricultureWeatherData>
{


    /**
     * 查询气象趋势数据（支持24小时、7天、30天）
     *
     * @param pastureId
     * @param batchId
     * @param range    时间范围类型，可选值："day"（24小时）、"week"（7天）、"month"（30天）
     * @return         包含x轴时间点和各气象参数趋势的Map数据
     */
    Map<String, Object> getTrendData(String pastureId, String batchId, String range);

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

    /**
     * 获取合并后的最新一条气象数据
     * @param pastureId 大棚ID
     * @return 最新气象数据
     */
    AgricultureWeatherData getLatestMergedWeatherData(String pastureId);
}
