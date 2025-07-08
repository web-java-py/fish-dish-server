package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCamera;
import com.server.domain.AgricultureClassAiReport;

/**
 * 摄像头参数Service接口
 * 
 * @author server
 * @date 2025-07-08
 */
public interface AgricultureCameraService extends IService<AgricultureCamera>
{
    /**
     * 查询摄像头参数
     * 
     * @param id 摄像头参数主键
     * @return 摄像头参数
     */
    public AgricultureCamera selectAgricultureCameraById(Long id);


    //根据设备id查询摄像头参数
    AgricultureCamera selectByDeviceId(Long deviceId);
    /**
     * 查询摄像头参数列表
     * 
     * @param agricultureCamera 摄像头参数
     * @return 摄像头参数集合
     */
    public List<AgricultureCamera> selectAgricultureCameraList(AgricultureCamera agricultureCamera);

    /**
     * 新增摄像头参数
     * 
     * @param agricultureCamera 摄像头参数
     * @return 结果
     */
    public int insertAgricultureCamera(AgricultureCamera agricultureCamera);

    /**
     * 修改摄像头参数
     * 
     * @param agricultureCamera 摄像头参数
     * @return 结果
     */
    public int updateAgricultureCamera(AgricultureCamera agricultureCamera);

    /**
     * 批量删除摄像头参数
     * 
     * @param ids 需要删除的摄像头参数主键集合
     * @return 结果
     */
    public int deleteAgricultureCameraByIds(Long[] ids);

    /**
     * 删除摄像头参数信息
     * 
     * @param id 摄像头参数主键
     * @return 结果
     */
    public int deleteAgricultureCameraById(Long id);
}
