package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureTaskEmployee;

/**
 * 批次任务工人Service接口
 * 
 * @author server
 * @date 2025-06-10
 */
public interface AgricultureTaskEmployeeService extends IService<AgricultureTaskEmployee>
{


    /**
     * 查询批次任务工人列表
     * 
     * @param agricultureTaskEmployee 批次任务工人
     * @return 批次任务工人集合
     */
    public List<AgricultureTaskEmployee> selectAgricultureTaskEmployeeList(AgricultureTaskEmployee agricultureTaskEmployee);

    /**
     * 新增批次任务工人
     * 
     * @param agricultureTaskEmployee 批次任务工人
     * @return 结果
     */
    public int insertAgricultureTaskEmployee(AgricultureTaskEmployee agricultureTaskEmployee);

    /**
     * 修改批次任务工人
     * 
     * @param agricultureTaskEmployee 批次任务工人
     * @return 结果
     */
    public int updateAgricultureTaskEmployee(AgricultureTaskEmployee agricultureTaskEmployee);

    /**
     * 批量删除批次任务工人
     * 
     * @param employee_id 需要删除的批次任务工人主键集合
     * @return 结果
     */
    public int deleteAgricultureTaskEmployeeByIds(Long employee_id);

    /**
     * 删除批次任务工人信息
     * 
     * @param id 批次任务工人主键
     * @return 结果
     */
    public int deleteAgricultureTaskEmployeeById(Long id);
}
