package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureDeviceSensorAlertMapper;
import com.server.domain.AgricultureDeviceSensorAlert;
import com.server.service.AgricultureDeviceSensorAlertService;

/**
 * 传感器预警信息Service业务层处理
 * 
 * @author server
 * @date 2025-05-26
 */
@Service
public class AgricultureDeviceSensorAlertServiceImpl extends ServiceImpl<AgricultureDeviceSensorAlertMapper, AgricultureDeviceSensorAlert> implements AgricultureDeviceSensorAlertService
{
    @Autowired
    private AgricultureDeviceSensorAlertMapper agricultureDeviceSensorAlertMapper;

    /**
     * 查询传感器预警信息
     * 
     * @param id 传感器预警信息主键
     * @return 传感器预警信息
     */
    @Override
    public AgricultureDeviceSensorAlert selectAgricultureDeviceSensorAlertById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询传感器预警信息列表
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 传感器预警信息
     */
    @Override
    public List<AgricultureDeviceSensorAlert> selectAgricultureDeviceSensorAlertList(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        return list();
    }

    /**
     * 新增传感器预警信息
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    @Override
    public int insertAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        agricultureDeviceSensorAlert.setCreateTime(LocalDateTime.now());
        return agricultureDeviceSensorAlertMapper.insert(agricultureDeviceSensorAlert);
    }

    /**
     * 修改传感器预警信息
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    @Override
    public int updateAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        agricultureDeviceSensorAlert.setUpdateTime(LocalDateTime.now());
        return agricultureDeviceSensorAlertMapper.updateById(agricultureDeviceSensorAlert);
    }

    /**
     * 批量删除传感器预警信息
     * 
     * @param ids 需要删除的传感器预警信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceSensorAlertByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除传感器预警信息信息
     * 
     * @param id 传感器预警信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceSensorAlertById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
