package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureThresholdConfig;

import java.util.List;

/**
 * 阈值配置Service接口
 * 
 * @author bxwy
 * @date 2025-06-08
 */
public interface AgricultureThresholdConfigService extends IService<AgricultureThresholdConfig>
{
    /**
     * 查询阈值配置
     * 
     * @param id 阈值配置主键
     * @return 阈值配置
     */
    public AgricultureThresholdConfig selectAgricultureThresholdConfigById(Long id);

    /**
     * 查询阈值配置
     *
     * @param deviceId 设备id
     * @return 该设备阈值配置集合
     */
    public List<AgricultureThresholdConfig> selectByDeviceId(Long deviceId);

    /**
     * 查询阈值配置列表
     * 
     * @param agricultureThresholdConfig 阈值配置
     * @return 阈值配置集合
     */
    public List<AgricultureThresholdConfig> selectAgricultureThresholdConfigList(AgricultureThresholdConfig agricultureThresholdConfig);

    /**
     * 新增阈值配置
     * 
     * @param agricultureThresholdConfig 阈值配置
     * @return 结果
     */
    public int insertAgricultureThresholdConfig(AgricultureThresholdConfig agricultureThresholdConfig);

    /**
     * 修改阈值配置
     * 
     * @param agricultureThresholdConfig 阈值配置
     * @return 结果
     */
    public int updateAgricultureThresholdConfig(AgricultureThresholdConfig agricultureThresholdConfig);

    /**
     * 批量删除阈值配置
     * 
     * @param ids 需要删除的阈值配置主键集合
     * @return 结果
     */
    public int deleteAgricultureThresholdConfigByIds(Long[] ids);

    /**
     * 删除阈值配置信息
     * 
     * @param id 阈值配置主键
     * @return 结果
     */
    public int deleteAgricultureThresholdConfigById(Long id);

    /**
     * 根据设备id集合查询阈值配置
     * @param deviceIds 设备id集合
     * @return 阈值配置集合
     */
    List<AgricultureThresholdConfig> selectByDeviceIds(List<Long> deviceIds);

}
