package com.server.service;

import java.util.List;
import com.server.domain.AgricultureDeviceType;

/**
 * 设备类型Service接口
 * 
 * @author server
 * @date 2025-06-20
 */
public interface AgricultureDeviceTypeService
{
    /**
     * 查询设备类型
     * 
     * @param id 设备类型主键
     * @return 设备类型
     */
    public AgricultureDeviceType selectAgricultureDeviceTypeById(Long id);

    /**
     * 查询设备类型列表
     * 
     * @param agricultureDeviceType 设备类型
     * @return 设备类型集合
     */
    public List<AgricultureDeviceType> selectAgricultureDeviceTypeList(AgricultureDeviceType agricultureDeviceType);

    /**
     * 新增设备类型
     * 
     * @param agricultureDeviceType 设备类型
     * @return 结果
     */
    public int insertAgricultureDeviceType(AgricultureDeviceType agricultureDeviceType);

    /**
     * 修改设备类型
     * 
     * @param agricultureDeviceType 设备类型
     * @return 结果
     */
    public int updateAgricultureDeviceType(AgricultureDeviceType agricultureDeviceType);

    /**
     * 批量删除设备类型
     * 
     * @param ids 需要删除的设备类型主键集合
     * @return 结果
     */
    public int deleteAgricultureDeviceTypeByIds(Long[] ids);

    /**
     * 删除设备类型信息
     * 
     * @param id 设备类型主键
     * @return 结果
     */
    public int deleteAgricultureDeviceTypeById(Long id);
}
