package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureCostEmployee;

/**
 * 人工工时Service接口
 * 
 * @author server
 * @date 2025-06-13
 */
public interface AgricultureCostEmployeeService extends IService<AgricultureCostEmployee>
{
    /**
     * 查询人工工时
     * 
     * @param costId 人工工时主键
     * @return 人工工时
     */
    public AgricultureCostEmployee selectAgricultureCostEmployeeByCostId(String costId);

    /**
     * 查询人工工时列表
     * 
     * @param agricultureCostEmployee 人工工时
     * @return 人工工时集合
     */
    public List<AgricultureCostEmployee> selectAgricultureCostEmployeeList(AgricultureCostEmployee agricultureCostEmployee);

    /**
     * 新增人工工时
     * 
     * @param agricultureCostEmployee 人工工时
     * @return 结果
     */
    public int insertAgricultureCostEmployee(AgricultureCostEmployee agricultureCostEmployee);

    /**
     * 修改人工工时
     * 
     * @param agricultureCostEmployee 人工工时
     * @return 结果
     */
    public int updateAgricultureCostEmployee(AgricultureCostEmployee agricultureCostEmployee);

    /**
     * 批量删除人工工时
     * 
     * @param costId 需要删除的人工工时主键集合
     * @return 结果
     */
    public int deleteAgricultureCostEmployeeByCostIds(Long costId);


}
