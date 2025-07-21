package com.server.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.core.text.Convert;
import com.server.domain.AgricultureDevice;
import com.server.fisco.bcos.AgricultureDeviceSensorAlertFB;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
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
public class AgricultureDeviceSensorAlertServiceImpl extends ServiceImpl<AgricultureDeviceSensorAlertMapper, AgricultureDeviceSensorAlert> implements AgricultureDeviceSensorAlertService
{


    private static final Logger log = LoggerFactory.getLogger(AgricultureDeviceSensorAlertServiceImpl.class);

    @Autowired
    private AgricultureDeviceSensorAlertMapper agricultureDeviceSensorAlertMapper;
    @Autowired(required = false)
    private Client client;

    @Value("${fisco.enabled}")
    private String fiscoEnabled;

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

        return list(queryWrapper);
    }

    /**
     * 新增传感器预警信息
     * 
     * @param agricultureDeviceSensorAlert 传感器预警信息
     * @return 结果
     */
    @Override
    public AgricultureDeviceSensorAlert insertAgricultureDeviceSensorAlert(AgricultureDeviceSensorAlert agricultureDeviceSensorAlert)
    {
        // 上链
        if (Convert.toBool(fiscoEnabled)) {
            try {
                AgricultureDeviceSensorAlertFB contract = AgricultureDeviceSensorAlertFB.deploy(
                        client, client.getCryptoSuite().getCryptoKeyPair());

                TransactionReceipt receipt = contract.SensorAlertCreate(
                        BigInteger.valueOf(agricultureDeviceSensorAlert.getId() == null ? 0L : agricultureDeviceSensorAlert.getId()),
                        agricultureDeviceSensorAlert.getAlertType(),
                        agricultureDeviceSensorAlert.getAlertMessage(),
                        agricultureDeviceSensorAlert.getParamName(),
                        agricultureDeviceSensorAlert.getParamValue(),
                        agricultureDeviceSensorAlert.getThresholdMax() == null ? BigInteger.ZERO : BigDecimal.valueOf(agricultureDeviceSensorAlert.getThresholdMax()).toBigInteger(),
                        agricultureDeviceSensorAlert.getThresholdMin() == null ? BigInteger.ZERO : BigDecimal.valueOf(agricultureDeviceSensorAlert.getThresholdMin()).toBigInteger(),
                        agricultureDeviceSensorAlert.getPastureId(),
                        agricultureDeviceSensorAlert.getBatchId(),
                        agricultureDeviceSensorAlert.getDeviceId() == null ? BigInteger.ZERO : BigInteger.valueOf(agricultureDeviceSensorAlert.getDeviceId()),
                        agricultureDeviceSensorAlert.getAlertTime() == null ? "" : agricultureDeviceSensorAlert.getAlertTime().toString(),
                        agricultureDeviceSensorAlert.getAlertLevel() == null ? BigInteger.ZERO : BigInteger.valueOf(agricultureDeviceSensorAlert.getAlertLevel())
                );
                // 存交易哈希
                agricultureDeviceSensorAlert.setBlockAddress(contract.getContractAddress());
                log.info("预警信息上链成功，交易哈希: {}", contract.getContractAddress());
            } catch (Exception e) {
                log.error("预警信息上链失败: {}", e.getMessage(), e);
            }
        }
        // 存数据库
        agricultureDeviceSensorAlert.setCreateTime(LocalDateTime.now());
        agricultureDeviceSensorAlertMapper.insert(agricultureDeviceSensorAlert);
        return agricultureDeviceSensorAlert; // 返回对象
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
