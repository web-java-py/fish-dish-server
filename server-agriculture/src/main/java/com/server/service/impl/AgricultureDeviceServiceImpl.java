package com.server.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.core.text.Convert;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceType;
import com.server.domain.AgriculturePasture;
import com.server.domain.vo.AgricultureDeviceVO;
import com.server.exception.ServiceException;
import com.server.fisco.bcos.AgricultureDeviceFB;
import com.server.mapper.*;
import com.server.service.AgricultureDeviceService;
import com.server.utils.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.server.constant.RabbitMQConstant.*;


/**
 * 设备信息Service业务层处理
 *
 * @author server
 * @date 2025-05-26
 */
@Service
public class AgricultureDeviceServiceImpl extends ServiceImpl<AgricultureDeviceMapper, AgricultureDevice> implements AgricultureDeviceService {
    @Autowired
    private AgricultureDeviceMapper agricultureDeviceMapper;
    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;
    @Autowired
    private AgricultureDeviceTypeMapper agricultureDeviceTypeMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired(required = false)
    private Client client;
    @Value("${fisco.enabled}")
    private String fiscoEnabled;

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public AgricultureDevice selectAgricultureDeviceById(String id) {
        return getById(id);
    }

    /**
     * 查询设备信息列表
     *
     * @param agricultureDevice 设备信息
     * @return VO设备信息列表
     */
    @Override
    public List<AgricultureDeviceVO> selectAgricultureDeviceListVO(AgricultureDevice agricultureDevice) {
        List<AgricultureDevice> deviceList = list();
        List<AgricultureDeviceVO> voList = new ArrayList<>();
        for (AgricultureDevice device : deviceList) {
            AgricultureDeviceVO vo = new AgricultureDeviceVO();
            BeanUtils.copyProperties(device, vo);
            // 查大棚名
            if (device.getPastureId() != null) {
                AgriculturePasture pasture = agriculturePastureMapper.selectById(device.getPastureId());
                vo.setPastureName(pasture != null ? pasture.getName() : null);
            }
            // 查分区名
            if (device.getBatchId() != null) {
                AgricultureCropBatch batch = agricultureCropBatchMapper.selectById(device.getBatchId());
                vo.setBatchName(batch != null ? batch.getBatchName() : null);
            }
            // 查设备类型
            if (device.getBatchId() != null) {
                AgricultureDeviceType type = agricultureDeviceTypeMapper.selectById(device.getDeviceTypeId());
                vo.setDeviceTypeName(type != null ? type.getTypeName() : null);
            }
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 查询设备信息列表
     *
     * @param agricultureDevice 设备信息
     * @return 设备信息
     */
    @Override
    public List<AgricultureDevice> selectAgricultureDeviceList(AgricultureDevice agricultureDevice) {
        return list();
    }

    /**
     * 新增设备信息
     *
     * @param agricultureDevice 设备信息
     * @return 结果
     */
    @Override
    public int insertAgricultureDevice(AgricultureDevice agricultureDevice) {
        if (Convert.toBool(fiscoEnabled)){
            try {
                /* 部署合约 拿到合约地址 */
                AgricultureDeviceFB agricultureDeviceFB = AgricultureDeviceFB.deploy(client, client.getCryptoSuite().getCryptoKeyPair());
                /* 调用合约 响应交易信息 */
                agricultureDeviceFB.createDevice(
                        agricultureDevice.getPastureId(),
                        agricultureDevice.getBatchId(),
                        agricultureDevice.getDeviceTypeId(),
                        agricultureDevice.getDeviceName()
                );
                agricultureDevice.setBlockAddress(agricultureDeviceFB.getContractAddress());
            } catch (ContractException e) {
                log.error(e.getMessage());
            }
        }
        return agricultureDeviceMapper.insert(agricultureDevice);
    }

    /**
     * 修改设备信息
     *
     * @param agricultureDevice 设备信息
     * @return 结果
     */
    @Override
    public int updateAgricultureDevice(AgricultureDevice agricultureDevice) {
        int update = agricultureDeviceMapper.updateById(agricultureDevice);
        String username;
        try {
            username = SecurityUtils.getUsername();
            if (org.apache.commons.lang3.StringUtils.isEmpty(username)) {
                username = "system";
            }
        } catch (Exception e) {
            username = "system";
        }
        agricultureDevice.setUpdateBy(username);

        rabbitTemplate.convertAndSend(FB_EXCHANGE, "*", agricultureDevice);
        return update;
    }

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceByIds(String[] ids) {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0; // removeByIds 返回 boolean
    }

    /**
     * 删除设备信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceById(String id) {
        return removeById(id) ? 1 : 0; // removeById 返回 boolean
    }
}
