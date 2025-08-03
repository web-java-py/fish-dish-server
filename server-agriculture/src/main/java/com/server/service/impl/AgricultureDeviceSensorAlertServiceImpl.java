package com.server.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.RabbitMQConstant;
import com.server.core.text.Convert;
import com.server.utils.SecurityUtils;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureDeviceSensorAlertMapper;
import com.server.domain.AgricultureDeviceSensorAlert;
import com.server.service.AgricultureDeviceSensorAlertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 传感器预警信息Service业务层处理
 *
 * @author server
 * @date 2025-05-26
 */
@Service
public class AgricultureDeviceSensorAlertServiceImpl extends ServiceImpl<AgricultureDeviceSensorAlertMapper, AgricultureDeviceSensorAlert> implements AgricultureDeviceSensorAlertService {


    private static final Logger log = LoggerFactory.getLogger(AgricultureDeviceSensorAlertServiceImpl.class);

    @Autowired
    private AgricultureDeviceSensorAlertMapper agricultureDeviceSensorAlertMapper;

    @Autowired(required = false)
    private Client client;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 查询传感器预警信息
     *
     * @param id 传感器预警信息主键
     * @return 传感器预警信息
     */
    @Override
    public AgricultureDeviceSensorAlert selectAgricultureDeviceSensorAlertById(Long id) {
        return getById(id);
    }

    /**
     * 查询传感器预警信息列表
     *
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 传感器预警信息
     */
    @Override
    public List<AgricultureDeviceSensorAlert> selectAgricultureDeviceSensorAlertList(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert) {
        LambdaQueryWrapper<AgricultureDeviceSensorAlert> queryWrapper = new LambdaQueryWrapper<>();

        //大棚 id 过滤
        if (agricultureDeviceSensorAlert.getPastureId() != null && !agricultureDeviceSensorAlert.getPastureId().isEmpty()) {
            queryWrapper.eq(AgricultureDeviceSensorAlert::getPastureId, agricultureDeviceSensorAlert.getPastureId());
        }
        //分区 id 过滤
        if (agricultureDeviceSensorAlert.getBatchId() != null && !agricultureDeviceSensorAlert.getBatchId().isEmpty()) {
            queryWrapper.eq(AgricultureDeviceSensorAlert::getBatchId, agricultureDeviceSensorAlert.getBatchId());
        }
        // 设备类型过滤
        if (agricultureDeviceSensorAlert.getDeviceType() != null && !agricultureDeviceSensorAlert.getDeviceType().isEmpty()) {
            queryWrapper.eq(AgricultureDeviceSensorAlert::getDeviceType, agricultureDeviceSensorAlert.getDeviceType());
        }
        // 报警级别过滤（0-警告，1-严重）
        if (agricultureDeviceSensorAlert.getAlertLevel() != null) {
            queryWrapper.eq(AgricultureDeviceSensorAlert::getAlertLevel, agricultureDeviceSensorAlert.getAlertLevel());
        }
        // 处理状态过滤（0未处理，1已处理）
        if (agricultureDeviceSensorAlert.getStatus() != null) {
            queryWrapper.eq(AgricultureDeviceSensorAlert::getStatus, agricultureDeviceSensorAlert.getStatus());
        }

        // 按预警时间倒序排列，最新的数据在前面
        queryWrapper.orderByDesc(AgricultureDeviceSensorAlert::getAlertTime);

        return list(queryWrapper);
    }

    /**
     * 新增传感器预警信息
     *
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    @Override
    public AgricultureDeviceSensorAlert insertAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert) {
        // 存数据库
        agricultureDeviceSensorAlert.setCreateTime(LocalDateTime.now());

        agricultureDeviceSensorAlertMapper.insert(agricultureDeviceSensorAlert);
        rabbitTemplate.convertAndSend(RabbitMQConstant.FB_EXCHANGE, "*", agricultureDeviceSensorAlert);
        return agricultureDeviceSensorAlert; // 返回对象
    }

    /**
     * 修改传感器预警信息
     *
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    @Override
    public int updateAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert) {
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
    public int deleteAgricultureDeviceSensorAlertByIds(Long[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除传感器预警信息信息
     *
     * @param id 传感器预警信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceSensorAlertById(Long id) {
        return removeById(id) ? 1 : 0;
    }
}
