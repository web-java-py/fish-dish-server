package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureMachine;
import com.server.mapper.AgricultureMachineMapper;
import com.server.service.AgricultureMachineService;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/26 16:29
 */
@Service
public class AgricultureMachineServiceImpl extends ServiceImpl<AgricultureMachineMapper, AgricultureMachine> implements AgricultureMachineService{

    @Autowired
    private AgricultureMachineMapper agricultureMachineMapper;

    /**
     * 查询农机信息
     * @param agricultureMachine
     * @return
     */
    @Override
    public List<AgricultureMachine> selectagricultureMachineList(AgricultureMachine agricultureMachine) {
        LambdaQueryWrapper<AgricultureMachine> lambdaQueryWrapper = new QueryWrapper<AgricultureMachine>().lambda();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(agricultureMachine.getMachineName()),AgricultureMachine::getMachineName,agricultureMachine.getMachineName());
        return agricultureMachineMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 删除农机信息
     * @param machineId
     * @return
     */
    @Override
    public int deleteById(Long machineId) {
        return agricultureMachineMapper.deleteById(machineId);
    }

    /**
     *新增农机信息
     */
    @Override
    public int addAgricultureMachine(AgricultureMachine agricultureMachine) {
        return agricultureMachineMapper.insert(agricultureMachine);
    }

    /**
     * 修改农机信息
     */
    @Override
    public int updateagricultureMaterial(AgricultureMachine agricultureMachine) {
        return agricultureMachineMapper.updateById(agricultureMachine);
    }
}