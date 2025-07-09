package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureCostEmployeeMapper;
import com.server.domain.AgricultureCostEmployee;
import com.server.service.AgricultureCostEmployeeService;

/**
 * 人工工时Service业务层处理
 *
 * @author server
 * @date 2025-06-13
 */
@Service
public class AgricultureCostEmployeeServiceImpl extends ServiceImpl<AgricultureCostEmployeeMapper,AgricultureCostEmployee> implements AgricultureCostEmployeeService
{
    @Autowired
    private AgricultureCostEmployeeMapper agricultureCostEmployeeMapper;

    /**
     * 查询人工工时
     *
     * @param costId 人工工时主键
     * @return 人工工时
     */
    @Override
    public AgricultureCostEmployee selectAgricultureCostEmployeeByCostId(String costId)
    {
        return agricultureCostEmployeeMapper.selectById(costId);
    }

    /**
     * 查询人工工时列表
     *
     * @param agricultureCostEmployee 人工工时
     * @return 人工工时
     */
    @Override
    public List<AgricultureCostEmployee> selectAgricultureCostEmployeeList(AgricultureCostEmployee agricultureCostEmployee)
    {
        LambdaQueryWrapper<AgricultureCostEmployee> lambdaQueryWrapper = new QueryWrapper<AgricultureCostEmployee>().lambda();
        if (agricultureCostEmployee.getTaskId() != null) {
            lambdaQueryWrapper.eq(AgricultureCostEmployee::getTaskId, agricultureCostEmployee.getTaskId());
        }
        return agricultureCostEmployeeMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增人工工时
     *
     * @param agricultureCostEmployee 人工工时
     * @return 结果
     */
    @Override
    public int insertAgricultureCostEmployee(AgricultureCostEmployee agricultureCostEmployee)
    {
        return agricultureCostEmployeeMapper.insert(agricultureCostEmployee);
    }

    /**
     * 修改人工工时
     *
     * @param agricultureCostEmployee 人工工时
     * @return 结果
     */
    @Override
    public int updateAgricultureCostEmployee(AgricultureCostEmployee agricultureCostEmployee)
    {
        return agricultureCostEmployeeMapper.updateById(agricultureCostEmployee);
    }

    /**
     * 批量删除人工工时
     *
     * @param costId 需要删除的人工工时主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCostEmployeeByCostIds(Long costId)
    {
        return agricultureCostEmployeeMapper.deleteById(costId);
    }
}
