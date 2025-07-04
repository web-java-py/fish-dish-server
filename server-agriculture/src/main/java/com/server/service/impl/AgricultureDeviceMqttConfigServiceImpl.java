package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureDeviceMqttConfigMapper;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.service.AgricultureDeviceMqttConfigService;

/**
 * 设备MQTT配置Service业务层处理
 * 
 * @author server
 * @date 2025-06-26
 */
@Service
public class AgricultureDeviceMqttConfigServiceImpl  extends ServiceImpl<AgricultureDeviceMqttConfigMapper, AgricultureDeviceMqttConfig> implements AgricultureDeviceMqttConfigService
{
    @Autowired
    private AgricultureDeviceMqttConfigMapper agricultureDeviceMqttConfigMapper;

    /**
     * 查询设备MQTT配置
     * 
     * @param id 设备MQTT配置主键
     * @return 设备MQTT配置
     */
    @Override
    public AgricultureDeviceMqttConfig selectAgricultureDeviceMqttConfigById(String id)
    {
        return getById(id);
    }

    // 根据设备id查询mqtt配置
    @Override
    public AgricultureDeviceMqttConfig getByDeviceId(Long deviceId) {
        return lambdaQuery()
                .eq(AgricultureDeviceMqttConfig::getDeviceId, deviceId)
                .one();
    }


    /**
     * 查询设备MQTT配置列表
     * 
     * @param agricultureDeviceMqttConfig 设备MQTT配置
     * @return 设备MQTT配置
     */
    @Override
    public List<AgricultureDeviceMqttConfig> selectAgricultureDeviceMqttConfigList(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        return list();
    }

    /**
     * 新增设备MQTT配置
     * 
     * @param agricultureDeviceMqttConfig 设备MQTT配置
     * @return 结果
     */
    @Override
    public int insertAgricultureDeviceMqttConfig(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        agricultureDeviceMqttConfig.setCreateTime(LocalDateTime.now());
        return agricultureDeviceMqttConfigMapper.insert(agricultureDeviceMqttConfig);
    }

    /**
     * 修改设备MQTT配置
     * 
     * @param agricultureDeviceMqttConfig 设备MQTT配置
     * @return 结果
     */
    @Override
    public int updateAgricultureDeviceMqttConfig(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig)
    {
        agricultureDeviceMqttConfig.setUpdateTime(LocalDateTime.now());
        return updateById(agricultureDeviceMqttConfig) ? 1 : 0;
    }

    /**
     * 批量删除设备MQTT配置
     * 
     * @param ids 需要删除的设备MQTT配置主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceMqttConfigByIds(String[] ids)
    {
        return  removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除设备MQTT配置信息
     * 
     * @param id 设备MQTT配置主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceMqttConfigById(String id)
    {
        return removeById(id) ? 1 : 0;
    }
}
