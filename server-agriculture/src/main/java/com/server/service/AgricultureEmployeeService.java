package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureEmployee;

/**
 * 雇员Service接口
 * 
 * @author server
 * @date 2025-06-10
 */
public interface AgricultureEmployeeService extends IService<AgricultureEmployee>
{
    /**
     * 查询雇员
     * 
     * @param employeeId 雇员主键
     * @return 雇员
     */
    public AgricultureEmployee selectAgricultureEmployeeByEmployeeId(String employeeId);

    /**
     * 查询雇员列表
     * 
     * @param agricultureEmployee 雇员
     * @return 雇员集合
     */
    public List<AgricultureEmployee> selectAgricultureEmployeeList(AgricultureEmployee agricultureEmployee);

    /**
     * 新增雇员
     * 
     * @param agricultureEmployee 雇员
     * @return 结果
     */
    public int insertAgricultureEmployee(AgricultureEmployee agricultureEmployee);

    /**
     * 修改雇员
     * 
     * @param agricultureEmployee 雇员
     * @return 结果
     */
    public int updateAgricultureEmployee(AgricultureEmployee agricultureEmployee);

    /**
     * 批量删除雇员
     * 
     * @param employeeIds 需要删除的雇员主键集合
     * @return 结果
     */
    public int deleteAgricultureEmployeeByEmployeeIds(String[] employeeIds);

    /**
     * 删除雇员信息
     * 
     * @param employeeId 雇员主键
     * @return 结果
     */
    public int deleteAgricultureEmployeeByEmployeeId(String employeeId);
}
