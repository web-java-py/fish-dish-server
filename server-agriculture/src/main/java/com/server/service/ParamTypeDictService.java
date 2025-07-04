package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureDeviceMqttConfig;
import com.server.domain.ParamTypeDict;

/**
 * 传感器参数类型中英文对照Service接口
 * 
 * @author bxwy
 * @date 2025-06-28
 */
public interface ParamTypeDictService extends IService<ParamTypeDict>
{
    /**
     * 查询传感器参数类型中英文对照
     * 
     * @param id 传感器参数类型中英文对照主键
     * @return 传感器参数类型中英文对照
     */
    public ParamTypeDict selectParamTypeDictById(Long id);

    /**
     * 查询传感器参数类型中英文对照列表
     * 
     * @param paramTypeDict 传感器参数类型中英文对照
     * @return 传感器参数类型中英文对照集合
     */
    public List<ParamTypeDict> selectParamTypeDictList(ParamTypeDict paramTypeDict);

    /**
     * 新增传感器参数类型中英文对照
     * 
     * @param paramTypeDict 传感器参数类型中英文对照
     * @return 结果
     */
    public int insertParamTypeDict(ParamTypeDict paramTypeDict);

    /**
     * 修改传感器参数类型中英文对照
     * 
     * @param paramTypeDict 传感器参数类型中英文对照
     * @return 结果
     */
    public int updateParamTypeDict(ParamTypeDict paramTypeDict);

    /**
     * 批量删除传感器参数类型中英文对照
     * 
     * @param ids 需要删除的传感器参数类型中英文对照主键集合
     * @return 结果
     */
    public int deleteParamTypeDictByIds(Long[] ids);

    /**
     * 删除传感器参数类型中英文对照信息
     * 
     * @param id 传感器参数类型中英文对照主键
     * @return 结果
     */
    public int deleteParamTypeDictById(Long id);
}
