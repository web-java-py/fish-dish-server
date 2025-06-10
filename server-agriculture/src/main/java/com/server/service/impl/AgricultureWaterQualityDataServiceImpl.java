package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureWaterQualityData;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureWaterQualityDataMapper;
import com.server.service.AgricultureWaterQualityDataService;

/**
 * 水质数据Service业务层处理
 * 
 * @author server
 * @date 2025-06-08
 */
@Service
public class AgricultureWaterQualityDataServiceImpl extends ServiceImpl<AgricultureWaterQualityDataMapper, AgricultureWaterQualityData> implements AgricultureWaterQualityDataService
{
    @Autowired
    private AgricultureWaterQualityDataMapper agricultureWaterQualityDataMapper;

    /**
     * 查询水质数据
     * 
     * @param id 水质数据主键
     * @return 水质数据
     */
    @Override
    public AgricultureWaterQualityData selectAgricultureWaterQualityDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询水质数据列表
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 水质数据
     */
    @Override
    public List<AgricultureWaterQualityData> selectAgricultureWaterQualityDataList(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        return list();
    }

    /**
     * 新增水质数据
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 结果
     */
    @Override
    public int insertAgricultureWaterQualityData(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        agricultureWaterQualityData.setCreateTime(LocalDateTime.now());
        return agricultureWaterQualityDataMapper.insert(agricultureWaterQualityData);
    }

    /**
     * 修改水质数据
     * 
     * @param agricultureWaterQualityData 水质数据
     * @return 结果
     */
    @Override
    public int updateAgricultureWaterQualityData(AgricultureWaterQualityData agricultureWaterQualityData)
    {
        agricultureWaterQualityData.setUpdateTime(LocalDateTime.now());
        return updateById(agricultureWaterQualityData) ? 1 : 0;
    }

    /**
     * 批量删除水质数据
     * 
     * @param ids 需要删除的水质数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWaterQualityDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除水质数据信息
     * 
     * @param id 水质数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWaterQualityDataById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
