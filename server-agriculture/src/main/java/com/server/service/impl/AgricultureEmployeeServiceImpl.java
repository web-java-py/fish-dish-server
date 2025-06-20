package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureTaskEmployee;
import com.server.mapper.AgricultureTaskEmployeeMapper;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureEmployeeMapper;
import com.server.domain.AgricultureEmployee;
import com.server.service.AgricultureEmployeeService;

/**
 * 雇员Service业务层处理
 * 
 * @author server
 * @date 2025-06-10
 */
@Service
public class AgricultureEmployeeServiceImpl extends ServiceImpl<AgricultureEmployeeMapper,AgricultureEmployee> implements AgricultureEmployeeService
{
    @Autowired
    private AgricultureEmployeeMapper agricultureEmployeeMapper;

    /**
     * 查询雇员
     * 
     * @param employeeId 雇员主键
     * @return 雇员
     */
    @Override
    public AgricultureEmployee selectAgricultureEmployeeByEmployeeId(String employeeId)
    {
        return agricultureEmployeeMapper.selectById(employeeId);
    }

    /**
     * 查询雇员列表
     * 
     * @param agricultureEmployee 雇员
     * @return 雇员
     */
    @Override
    public List<AgricultureEmployee> selectAgricultureEmployeeList(AgricultureEmployee agricultureEmployee)
    {
        LambdaQueryWrapper<AgricultureEmployee> lambdaQueryWrapper = new QueryWrapper<AgricultureEmployee>().lambda();
        return agricultureEmployeeMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增雇员
     * 
     * @param agricultureEmployee 雇员
     * @return 结果
     */
    @Override
    public int insertAgricultureEmployee(AgricultureEmployee agricultureEmployee)
    {

        return agricultureEmployeeMapper.insert(agricultureEmployee);
    }

    /**
     * 修改雇员
     * 
     * @param agricultureEmployee 雇员
     * @return 结果
     */
    @Override
    public int updateAgricultureEmployee(AgricultureEmployee agricultureEmployee)
    {

        return agricultureEmployeeMapper.updateById(agricultureEmployee);
    }

    /**
     * 批量删除雇员
     * 
     * @param employeeIds 需要删除的雇员主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureEmployeeByEmployeeIds(String[] employeeIds)
    {
        return agricultureEmployeeMapper.deleteById(employeeIds);
    }

    /**
     * 删除雇员信息
     * 
     * @param employeeId 雇员主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureEmployeeByEmployeeId(String employeeId)
    {
        return agricultureEmployeeMapper.deleteById(employeeId);
    }
}
