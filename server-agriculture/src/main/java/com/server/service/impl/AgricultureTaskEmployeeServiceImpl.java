package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureTaskEmployeeMapper;
import com.server.domain.AgricultureTaskEmployee;
import com.server.service.AgricultureTaskEmployeeService;

/**
 * 批次任务工人Service业务层处理
 * 
 * @author server
 * @date 2025-06-10
 */
@Service
public class AgricultureTaskEmployeeServiceImpl extends ServiceImpl<AgricultureTaskEmployeeMapper,AgricultureTaskEmployee> implements AgricultureTaskEmployeeService
{
    @Autowired
    private AgricultureTaskEmployeeMapper agricultureTaskEmployeeMapper;

    /**
     * 查询批次任务工人列表
     * 
     * @param agricultureTaskEmployee 批次任务工人
     * @return 批次任务工人
     */
    @Override
    public List<AgricultureTaskEmployee> selectAgricultureTaskEmployeeList(AgricultureTaskEmployee agricultureTaskEmployee)
    {
        if (agricultureTaskEmployee != null && agricultureTaskEmployee.getTaskId() != null) {
            // 如果传入了taskId，使用自定义的selectGYID方法
            return agricultureTaskEmployeeMapper.selectGYID(agricultureTaskEmployee.getTaskId());
        } else {
            // 如果没有传入taskId，使用默认的查询方式
            LambdaQueryWrapper<AgricultureTaskEmployee> lambdaQueryWrapper = new QueryWrapper<AgricultureTaskEmployee>().lambda();
            return agricultureTaskEmployeeMapper.selectList(lambdaQueryWrapper);
        }
    }

    /**
     * 新增批次任务工人
     * 
     * @param agricultureTaskEmployee 批次任务工人
     * @return 结果
     */
    @Override
    public int insertAgricultureTaskEmployee(AgricultureTaskEmployee agricultureTaskEmployee)
    {
        return agricultureTaskEmployeeMapper.insert(agricultureTaskEmployee);
    }

    /**
     * 修改批次任务工人
     * 
     * @param agricultureTaskEmployee 批次任务工人
     * @return 结果
     */
    @Override
    public int updateAgricultureTaskEmployee(AgricultureTaskEmployee agricultureTaskEmployee)
    {
        return agricultureTaskEmployeeMapper.updateById(agricultureTaskEmployee);
    }

    /**
     * 批量删除批次任务工人
     * 
     * @param employee_id 需要删除的批次任务工人主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureTaskEmployeeByIds(Long employee_id)
    {
        return agricultureTaskEmployeeMapper.deleteById(employee_id);
    }

    /**
     * 删除批次任务工人信息
     * 
     * @param id 批次任务工人主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureTaskEmployeeById(Long id)
    {
        return agricultureTaskEmployeeMapper.deleteById(id);
    }
}
