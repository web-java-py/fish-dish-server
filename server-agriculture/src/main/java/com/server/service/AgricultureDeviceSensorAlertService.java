package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceSensorAlert;

/**
 * 传感器预警信息Service接口
 * 
 * @author bxwy
 * @date 2025-05-26
 */
public interface AgricultureDeviceSensorAlertService extends IService<AgricultureDeviceSensorAlert>
{
    /**
     * 查询传感器预警信息
     * 
     * @param id 传感器预警信息主键
     * @return 传感器预警信息
     */
    public AgricultureDeviceSensorAlert selectAgricultureDeviceSensorAlertById(Long id);

    /**
     * 查询传感器预警信息列表
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 传感器预警信息集合
     */
    public List<AgricultureDeviceSensorAlert> selectAgricultureDeviceSensorAlertList(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert);

    /**
     * 新增传感器预警信息
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    public AgricultureDeviceSensorAlert insertAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert);

    /**
     * 修改传感器预警信息
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    public int updateAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert);

    /**
     * 批量删除传感器预警信息
     * 
     * @param ids 需要删除的传感器预警信息主键集合
     * @return 结果
     */
    public int deleteAgricultureDeviceSensorAlertByIds(Long[] ids);

    /**
     * 删除传感器预警信息信息
     * 
     * @param id 传感器预警信息主键
     * @return 结果
     */
    public int deleteAgricultureDeviceSensorAlertById(Long id);
}
