package com.server.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureWaterQualityData;
import com.server.mapper.AgricultureWaterQualityDataMapper;
import com.server.service.AgricultureWaterQualityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.ParamTypeDictMapper;
import com.server.domain.ParamTypeDict;
import com.server.service.ParamTypeDictService;

/**
 * 传感器参数类型中英文对照Service业务层处理
 * 
 * @author server
 * @date 2025-06-28
 */
@Service
public class ParamTypeDictServiceImpl extends ServiceImpl<ParamTypeDictMapper, ParamTypeDict> implements ParamTypeDictService
{
    @Autowired
    private ParamTypeDictMapper paramTypeDictMapper;

    /**
     * 查询传感器参数类型中英文对照
     * 
     * @param id 传感器参数类型中英文对照主键
     * @return 传感器参数类型中英文对照
     */
    @Override
    public ParamTypeDict selectParamTypeDictById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询传感器参数类型中英文对照列表
     * 
     * @param paramTypeDict 传感器参数类型中英文对照
     * @return 传感器参数类型中英文对照
     */
    @Override
    public List<ParamTypeDict> selectParamTypeDictList(ParamTypeDict paramTypeDict)
    {
        return list();
    }

    /**
     * 新增传感器参数类型中英文对照
     * 
     * @param paramTypeDict 传感器参数类型中英文对照
     * @return 结果
     */
    @Override
    public int insertParamTypeDict(ParamTypeDict paramTypeDict)
    {
        return paramTypeDictMapper.insert(paramTypeDict);
    }

    /**
     * 修改传感器参数类型中英文对照
     * 
     * @param paramTypeDict 传感器参数类型中英文对照
     * @return 结果
     */
    @Override
    public int updateParamTypeDict(ParamTypeDict paramTypeDict)
    {
        return updateById(paramTypeDict) ? 1 : 0;
    }

    /**
     * 批量删除传感器参数类型中英文对照
     * 
     * @param ids 需要删除的传感器参数类型中英文对照主键
     * @return 结果
     */
    @Override
    public int deleteParamTypeDictByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除传感器参数类型中英文对照信息
     * 
     * @param id 传感器参数类型中英文对照主键
     * @return 结果
     */
    @Override
    public int deleteParamTypeDictById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
