package com.server.service.impl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureWeatherData;
import com.server.mapper.AgricultureDeviceMapper;
import com.server.service.AgricultureDeviceService;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureWeatherDataMapper;
import com.server.service.AgricultureWeatherDataService;

/**
 * 气象数据Service业务层处理
 * 
 * @author server
 * @date 2025-06-08
 */
@Service
public class AgricultureWeatherDataServiceImpl extends ServiceImpl<AgricultureWeatherDataMapper, AgricultureWeatherData> implements AgricultureWeatherDataService
{
    @Autowired
    private AgricultureWeatherDataMapper agricultureWeatherDataMapper;

    /**
     * 查询气象数据
     * 
     * @param id 气象数据主键
     * @return 气象数据
     */
    @Override
    public AgricultureWeatherData selectAgricultureWeatherDataById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询气象数据列表
     * 
     * @param agricultureWeatherData 气象数据
     * @return 气象数据
     */
    @Override
    public List<AgricultureWeatherData> selectAgricultureWeatherDataList(AgricultureWeatherData agricultureWeatherData)
    {
        return list();
    }

    /**
     * 新增气象数据
     * 
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    @Override
    public int insertAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData)
    {
        agricultureWeatherData.setCreateTime(LocalDateTime.now());
        return agricultureWeatherDataMapper.insert(agricultureWeatherData);
    }

    /**
     * 修改气象数据
     * 
     * @param agricultureWeatherData 气象数据
     * @return 结果
     */
    @Override
    public int updateAgricultureWeatherData(AgricultureWeatherData agricultureWeatherData)
    {
        agricultureWeatherData.setUpdateTime(LocalDateTime.now());
        return  updateById(agricultureWeatherData) ? 1 : 0;
    }

    /**
     * 批量删除气象数据
     * 
     * @param ids 需要删除的气象数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWeatherDataByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除气象数据信息
     * 
     * @param id 气象数据主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureWeatherDataById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
