package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceMqttConfig;

/**
 * 设备MQTT配置Service接口
 * 
 * @author server
 * @date 2025-06-26
 */
public interface AgricultureDeviceMqttConfigService extends IService<AgricultureDeviceMqttConfig>
{
    /**
     * 查询设备MQTT配置
     * 
     * @param id 设备MQTT配置主键
     * @return 设备MQTT配置
     */
    public AgricultureDeviceMqttConfig selectAgricultureDeviceMqttConfigById(String id);

    // 根据设备id查询mqtt配置
    public AgricultureDeviceMqttConfig getByDeviceId(Long deviceId);

    /**
     * 查询设备MQTT配置列表
     * 
     * @param agricultureDeviceMqttConfig 设备MQTT配置
     * @return 设备MQTT配置集合
     */
    public List<AgricultureDeviceMqttConfig> selectAgricultureDeviceMqttConfigList(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig);

    /**
     * 新增设备MQTT配置
     * 
     * @param agricultureDeviceMqttConfig 设备MQTT配置
     * @return 结果
     */
    public int insertAgricultureDeviceMqttConfig(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig);

    /**
     * 修改设备MQTT配置
     * 
     * @param agricultureDeviceMqttConfig 设备MQTT配置
     * @return 结果
     */
    public int updateAgricultureDeviceMqttConfig(AgricultureDeviceMqttConfig agricultureDeviceMqttConfig);

    /**
     * 批量删除设备MQTT配置
     * 
     * @param ids 需要删除的设备MQTT配置主键集合
     * @return 结果
     */
    public int deleteAgricultureDeviceMqttConfigByIds(String[] ids);

    /**
     * 删除设备MQTT配置信息
     * 
     * @param id 设备MQTT配置主键
     * @return 结果
     */
    public int deleteAgricultureDeviceMqttConfigById(String id);
}
