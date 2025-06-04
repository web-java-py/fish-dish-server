package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.core.domain.AjaxResult;
import com.server.domain.AgricultureMachine;
import com.server.domain.AgricultureMaterial;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/26 16:25
 */public interface AgricultureMachineService extends IService<AgricultureMachine> {

    /**
     * 农机信息
     * @param agricultureMachine
     * @return
     */
    List<AgricultureMachine> selectagricultureMachineList(AgricultureMachine agricultureMachine);

    /**
     * 删除农机信息
     * @param machineId
     * @return
     */
    int deleteById(Long machineId);

    /**
     *新增农机信息
     */
    int addAgricultureMachine(AgricultureMachine agricultureMachine);

    /**
     * 修改农机信息
     */
    int updateagricultureMaterial(AgricultureMachine agricultureMachine);



}