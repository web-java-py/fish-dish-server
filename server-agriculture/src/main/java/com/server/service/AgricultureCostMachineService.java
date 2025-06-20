package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCostMachine;

/**
 * 机械工时Service接口
 * 
 * @author server
 * @date 2025-06-10
 */
public interface AgricultureCostMachineService extends IService<AgricultureCostMachine>
{
    /**
     * 查询机械工时
     * 
     * @param costId 机械工时主键
     * @return 机械工时
     */
    public AgricultureCostMachine selectAgricultureCostMachineByCostId(String costId);

    /**
     * 查询机械工时列表
     * 
     * @param agricultureCostMachine 机械工时
     * @return 机械工时集合
     */
    public List<AgricultureCostMachine> selectAgricultureCostMachineList(AgricultureCostMachine agricultureCostMachine);

    /**
     * 新增机械工时
     * 
     * @param agricultureCostMachine 机械工时
     * @return 结果
     */
    public int insertAgricultureCostMachine(AgricultureCostMachine agricultureCostMachine);

    /**
     * 修改机械工时
     * 
     * @param agricultureCostMachine 机械工时
     * @return 结果
     */
    public int updateAgricultureCostMachine(AgricultureCostMachine agricultureCostMachine);

    /**
     * 批量删除机械工时
     *
     * @param costId 需要删除的机械工时主键集合
     * @return 结果
     */
    public int deleteAgricultureCostMachineByCostIds(Long costId);



}
