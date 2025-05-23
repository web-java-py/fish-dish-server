package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureMaterial;
import com.server.mapper.AgricultureMaterialMapper;
import com.server.service.AgricultureMaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/23 17:22
 */
@Service
public class AgricultureMaterialServiceImpl extends ServiceImpl<AgricultureMaterialMapper, AgricultureMaterial> implements AgricultureMaterialService {


    @Autowired
    private AgricultureMaterialMapper agricultureMaterialInfoMapper;

    /**
     * 查询
     * @param agricultureMaterialInfo
     * @return
     */
    @Override
    public List<AgricultureMaterial> selectAgricultureMaterialInfoList(AgricultureMaterial agricultureMaterialInfo) {
        //lambda 不是对象名
        LambdaQueryWrapper<AgricultureMaterial> lambdaQueryWrapper = new QueryWrapper<AgricultureMaterial>().lambda();
        //获取模糊查询的条件
        lambdaQueryWrapper.like(AgricultureMaterial::getMaterialName, agricultureMaterialInfo.getMaterialName());
        return agricultureMaterialInfoMapper.selectList(lambdaQueryWrapper);
    }
}
