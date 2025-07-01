package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureThresholdConfig;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureThresholdConfigMapper;
import com.server.service.AgricultureThresholdConfigService;

/**
 * 阈值配置Service业务层处理
 * 
 * @author server
 * @date 2025-06-08
 */
@Service
public class AgricultureThresholdConfigServiceImpl extends ServiceImpl<AgricultureThresholdConfigMapper, AgricultureThresholdConfig> implements AgricultureThresholdConfigService
{
    @Autowired
    private AgricultureThresholdConfigMapper agricultureThresholdConfigMapper;

    /**
     * 查询阈值配置
     * 
     * @param id 阈值配置主键
     * @return 阈值配置
     */
    @Override
    public AgricultureThresholdConfig selectAgricultureThresholdConfigById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询阈值配置列表
     * 
     * @param agricultureThresholdConfig 阈值配置
     * @return 阈值配置
     */
    @Override
    public List<AgricultureThresholdConfig> selectAgricultureThresholdConfigList(AgricultureThresholdConfig agricultureThresholdConfig)
    {
        return list();
    }

    /**
     * 查询阈值配置列表
     *
     * @param deviceId 设备Id
     * @return 该设备阈值配置
     */
    @Override
    public List<AgricultureThresholdConfig> selectByDeviceId(Long deviceId) {
        return lambdaQuery().eq(AgricultureThresholdConfig::getDeviceId, deviceId).list();
    }
    /**
     * 新增阈值配置
     * 
     * @param agricultureThresholdConfig 阈值配置
     * @return 结果
     */
    @Override
    public int insertAgricultureThresholdConfig(AgricultureThresholdConfig agricultureThresholdConfig)
    {
        agricultureThresholdConfig.setCreateTime(LocalDateTime.now());
        return agricultureThresholdConfigMapper.insert(agricultureThresholdConfig);
    }

    /**
     * 修改阈值配置
     * 
     * @param agricultureThresholdConfig 阈值配置
     * @return 结果
     */
    @Override
    public int updateAgricultureThresholdConfig(AgricultureThresholdConfig agricultureThresholdConfig)
    {
        agricultureThresholdConfig.setUpdateTime(LocalDateTime.now());
        return updateById(agricultureThresholdConfig) ? 1 : 0;
    }

    /**
     * 批量删除阈值配置
     * 
     * @param ids 需要删除的阈值配置主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureThresholdConfigByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除阈值配置信息
     * 
     * @param id 阈值配置主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureThresholdConfigById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }

    /**
     * 根据设备ID集合查询所有对应的阈值配置
     * @param deviceIds 设备ID集合
     * @return 阈值配置列表，如果deviceIds为空则返回空列表
     */
    @Override
    public List<AgricultureThresholdConfig> selectByDeviceIds(List<Long> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) {
            return Collections.emptyList();
        }
        return lambdaQuery().in(AgricultureThresholdConfig::getDeviceId, deviceIds).list();
    }

}
