package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureAutoControlStrategy;
import com.server.domain.AgricultureDevice;

/**
 * 设备自动调节策略Service接口
 * 
 * @author bxwy
 * @date 2025-07-02
 */
public interface AgricultureAutoControlStrategyService extends IService<AgricultureAutoControlStrategy>
{
    /**
     * 查询设备自动调节策略
     * 
     * @param id 设备自动调节策略主键
     * @return 设备自动调节策略
     */
    public AgricultureAutoControlStrategy selectAgricultureAutoControlStrategyById(Long id);

    /**
     * 查询设备自动调节策略列表
     * 
     * @param agricultureAutoControlStrategy 设备自动调节策略
     * @return 设备自动调节策略集合
     */
    public List<AgricultureAutoControlStrategy> selectAgricultureAutoControlStrategyList(AgricultureAutoControlStrategy agricultureAutoControlStrategy);

    /**
     * 新增设备自动调节策略
     * 
     * @param agricultureAutoControlStrategy 设备自动调节策略
     * @return 结果
     */
    public int insertAgricultureAutoControlStrategy(AgricultureAutoControlStrategy agricultureAutoControlStrategy);

    /**
     * 修改设备自动调节策略
     * 
     * @param agricultureAutoControlStrategy 设备自动调节策略
     * @return 结果
     */
    public int updateAgricultureAutoControlStrategy(AgricultureAutoControlStrategy agricultureAutoControlStrategy);

    /**
     * 批量删除设备自动调节策略
     * 
     * @param ids 需要删除的设备自动调节策略主键集合
     * @return 结果
     */
    public int deleteAgricultureAutoControlStrategyByIds(Long[] ids);

    /**
     * 删除设备自动调节策略信息
     * 
     * @param id 设备自动调节策略主键
     * @return 结果
     */
    public int deleteAgricultureAutoControlStrategyById(Long id);
}
