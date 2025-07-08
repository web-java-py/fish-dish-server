package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureDevice;
import com.server.domain.vo.AgricultureDeviceVO;

/**
 * 设备信息Service接口
 * 
 * @author bxwy
 * @date 2025-05-26
 */
public interface AgricultureDeviceService extends IService<AgricultureDevice>
{
    /**
     * 查询设备信息
     * 
     * @param id 设备信息主键
     * @return 设备信息
     */
    public AgricultureDevice selectAgricultureDeviceById(String id);

    /**
     * 查询设备信息列表
     * 
     * @param agricultureDevice 设备信息
     * @return 设备信息VO集合
     */
    public List<AgricultureDeviceVO> selectAgricultureDeviceListVO(AgricultureDevice agricultureDevice);

    /**
     * 查询设备信息列表
     *
     * @param agricultureDevice 设备信息
     * @return 设备信息集合
     */
    public List<AgricultureDevice> selectAgricultureDeviceList(AgricultureDevice agricultureDevice);

    /**
     * 新增设备信息
     * 
     * @param agricultureDevice 设备信息
     * @return 结果
     */
    public Long insertAgricultureDevice(AgricultureDevice agricultureDevice);

    /**
     * 修改设备信息
     * 
     * @param agricultureDevice 设备信息
     * @return 结果
     */
    public int updateAgricultureDevice(AgricultureDevice agricultureDevice);

    /**
     * 批量删除设备信息
     * 
     * @param ids 需要删除的设备信息主键集合
     * @return 结果
     */
    public int deleteAgricultureDeviceByIds(String[] ids);

    /**
     * 删除设备信息信息
     * 
     * @param id 设备信息主键
     * @return 结果
     */
    public int deleteAgricultureDeviceById(String id);

    /**
     * 根据大棚id和分区id查询设备id集合
     */
    List<Long> selectDeviceIdsByPastureAndBatch(Long pastureId, Long batchId);
}
