package com.server.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureDeviceTypeMapper;
import com.server.domain.AgricultureDeviceType;
import com.server.service.AgricultureDeviceTypeService;

/**
 * 设备类型Service业务层处理
 * 
 * @author server
 * @date 2025-06-20
 */
@Service
public class AgricultureDeviceTypeServiceImpl extends ServiceImpl<AgricultureDeviceTypeMapper, AgricultureDeviceType> implements AgricultureDeviceTypeService
{
    @Autowired
    private AgricultureDeviceTypeMapper agricultureDeviceTypeMapper;

    /**
     * 查询设备类型
     * 
     * @param id 设备类型主键
     * @return 设备类型
     */
    @Override
    public AgricultureDeviceType selectAgricultureDeviceTypeById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询设备类型列表
     * 
     * @param agricultureDeviceType 设备类型
     * @return 设备类型
     */
    @Override
    public List<AgricultureDeviceType> selectAgricultureDeviceTypeList(AgricultureDeviceType agricultureDeviceType)
    {
        return list();
    }

    /**
     * 新增设备类型
     * 
     * @param agricultureDeviceType 设备类型
     * @return 结果
     */
    @Override
    public int insertAgricultureDeviceType(AgricultureDeviceType agricultureDeviceType)
    {
        return agricultureDeviceTypeMapper.insert(agricultureDeviceType);
    }

    /**
     * 修改设备类型
     * 
     * @param agricultureDeviceType 设备类型
     * @return 结果
     */
    @Override
    public int updateAgricultureDeviceType(AgricultureDeviceType agricultureDeviceType)
    {
        return agricultureDeviceTypeMapper.updateById(agricultureDeviceType);
    }

    /**
     * 批量删除设备类型
     * 
     * @param ids 需要删除的设备类型主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceTypeByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除设备类型信息
     * 
     * @param id 设备类型主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureDeviceTypeById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
