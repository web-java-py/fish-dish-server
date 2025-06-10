package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgriculturePasture;
import com.server.domain.vo.AgricultureDeviceVO;
import com.server.mapper.AgricultureClassMapper;
import com.server.mapper.AgricultureCropBatchMapper;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.mapper.AgriculturePastureMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 设备信息Service业务层处理
 *
 * @author server
 * @date 2025-05-26
 */
@Service
public class AgricultureDeviceServiceImpl extends ServiceImpl<AgricultureDeviceMapper, AgricultureDevice> implements AgricultureDeviceService
{
    @Autowired
    private AgricultureDeviceMapper agricultureDeviceMapper;
    @Autowired
    private AgriculturePastureMapper agriculturePastureMapper;
    @Autowired
    private AgricultureCropBatchMapper agricultureCropBatchMapper;

    /**
     * 查询设备信息
     *
     * @param id 设备信息主键
     * @return 设备信息
     */
    @Override
    public AgricultureDevice selectAgricultureDeviceById(String id)
    {
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
    public int insertAgricultureDevice(AgricultureDevice agricultureDevice)
    {
        if (agricultureDevice.getDeviceType() == null) {
            agricultureDevice.setDeviceType("传感器");  // 设置默认值
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
    public int updateAgricultureDevice(AgricultureDevice agricultureDevice)
    {
        agricultureDevice.setUpdateTime(LocalDateTime.now());
        return updateById(agricultureDevice) ? 1 : 0; // updateById 返回 boolean
    }

    /**
     * 批量删除设备信息
     *
     * @param ids 需要删除的设备信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceByIds(String[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0; // removeByIds 返回 boolean
    }

    /**
     * 删除设备信息
     *
     * @param id 设备信息主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceById(String id)
    {
        return removeById(id) ? 1 : 0; // removeById 返回 boolean
    }
}