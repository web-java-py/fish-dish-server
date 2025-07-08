package com.server.service.impl;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureClass;
import com.server.mapper.AgricultureClassMapper;
import com.server.service.AgricultureClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureCameraMapper;
import com.server.domain.AgricultureCamera;
import com.server.service.AgricultureCameraService;

/**
 * 摄像头参数Service业务层处理
 * 
 * @author server
 * @date 2025-07-08
 */
@Service
public class AgricultureCameraServiceImpl extends ServiceImpl<AgricultureCameraMapper, AgricultureCamera> implements AgricultureCameraService
{
    @Autowired
    private AgricultureCameraMapper agricultureCameraMapper;

    /**
     * 查询摄像头参数
     * 
     * @param id 摄像头参数主键
     * @return 摄像头参数
     */
    @Override
    public AgricultureCamera selectAgricultureCameraById(Long id)
    {
        return getById(id);
    }
    //根据设备id查询摄像头参数
    @Override
    public AgricultureCamera selectByDeviceId(Long deviceId) {
        return lambdaQuery().eq(AgricultureCamera::getDeviceId, deviceId).one();
    }
    /**
     * 查询摄像头参数列表
     * 
     * @param agricultureCamera 摄像头参数
     * @return 摄像头参数
     */
    @Override
    public List<AgricultureCamera> selectAgricultureCameraList(AgricultureCamera agricultureCamera)
    {
        return list();
    }

    /**
     * 新增摄像头参数
     * 
     * @param agricultureCamera 摄像头参数
     * @return 结果
     */
    @Override
    public int insertAgricultureCamera(AgricultureCamera agricultureCamera)
    {
        return agricultureCameraMapper.insert(agricultureCamera);
    }

    /**
     * 修改摄像头参数
     * 
     * @param agricultureCamera 摄像头参数
     * @return 结果
     */
    @Override
    public int updateAgricultureCamera(AgricultureCamera agricultureCamera)
    {
        return updateById(agricultureCamera) ? 1 : 0;
    }

    /**
     * 批量删除摄像头参数
     * 
     * @param ids 需要删除的摄像头参数主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCameraByIds(Long[] ids)
    {
        return removeByIds(Arrays.asList(ids)) ? ids.length : 0;
    }

    /**
     * 删除摄像头参数信息
     * 
     * @param id 摄像头参数主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCameraById(Long id)
    {
        return removeById(id) ? 1 : 0;
    }
}
