package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureMaterial;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/23 17:18
 *
 * 农资管理
 */
public interface AgricultureMaterialService extends IService<AgricultureMaterial> {

    /**
     * 查询
     */
    List<AgricultureMaterial> selectAgricultureMaterialInfoList(AgricultureMaterial agricultureMaterialInfo);

    /**
     * 删除
     *
     * @return
     */
    int deleteById(Long materialId);

    /**
     * 新增
     * @param agricultureMaterial
     * @return
     */
    int addAgricultureMaterial(AgricultureMaterial agricultureMaterial);

    /**
     * 修改农资信息
     * @param agricultureMaterial
     * @return
     */
    int updateagricultureMaterial(AgricultureMaterial agricultureMaterial);

}
