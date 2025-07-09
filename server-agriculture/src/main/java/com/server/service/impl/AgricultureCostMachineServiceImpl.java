package com.server.service.impl;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureEmployee;
import com.server.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.server.mapper.AgricultureCostMachineMapper;
import com.server.domain.AgricultureCostMachine;
import com.server.service.AgricultureCostMachineService;

/**
 * 机械工时Service业务层处理
 * 
 * @author server
 * @date 2025-06-10
 */
@Service
public class AgricultureCostMachineServiceImpl extends ServiceImpl<AgricultureCostMachineMapper,AgricultureCostMachine> implements AgricultureCostMachineService
{
    @Autowired
    private AgricultureCostMachineMapper agricultureCostMachineMapper;

    /**
     * 查询机械工时
     * 
     * @param costId 机械工时主键
     * @return 机械工时
     */
    @Override
    public AgricultureCostMachine selectAgricultureCostMachineByCostId(String costId)
    {
        LambdaQueryWrapper<AgricultureCostMachine> lambdaQueryWrapper = new QueryWrapper<AgricultureCostMachine>().lambda();
        return agricultureCostMachineMapper.selectById(costId);
    }

    /**
     * 查询机械工时列表
     * 
     * @param agricultureCostMachine 机械工时
     * @return 机械工时
     */
    @Override
    public List<AgricultureCostMachine> selectAgricultureCostMachineList(AgricultureCostMachine agricultureCostMachine)
    {
        LambdaQueryWrapper<AgricultureCostMachine> lambdaQueryWrapper = new QueryWrapper<AgricultureCostMachine>().lambda();
        // 新增：如果传入的agricultureCostMachine包含taskId，则加上where条件
        if (agricultureCostMachine.getTaskId() != null) {
            lambdaQueryWrapper.eq(AgricultureCostMachine::getTaskId, agricultureCostMachine.getTaskId());
        }
        //将 cost_id 倒叙，这样子就能看到最新的机械工时数据
        lambdaQueryWrapper.orderByDesc(AgricultureCostMachine::getCostId);
        return agricultureCostMachineMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增机械工时
     * 
     * @param agricultureCostMachine 机械工时
     * @return 结果
     */
    @Override
    public int insertAgricultureCostMachine(AgricultureCostMachine agricultureCostMachine)
    {
        return agricultureCostMachineMapper.insert(agricultureCostMachine);
    }

    /**
     * 修改机械工时
     * 
     * @param agricultureCostMachine 机械工时
     * @return 结果
     */
    @Override
    public int updateAgricultureCostMachine(AgricultureCostMachine agricultureCostMachine)
    {
        return agricultureCostMachineMapper.updateById(agricultureCostMachine);
    }

    /**
     * 删除机械工时
     *
     * @param costId 需要删除的机械工时主键
     * @return 结果
     */
    @Override
    public int deleteAgricultureCostMachineByCostIds(Long costId)
    {
        return agricultureCostMachineMapper.deleteById(costId);
    }

}
