package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureMaterial;
import com.server.exception.ServiceException;
import com.server.mapper.AgricultureMaterialMapper;
import com.server.service.AgricultureMaterialService;
import com.server.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
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
    private AgricultureMaterialMapper agricultureMaterialMapper;

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
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(agricultureMaterialInfo.getMaterialName()),AgricultureMaterial::getMaterialName, agricultureMaterialInfo.getMaterialName());
        return agricultureMaterialMapper.selectList(lambdaQueryWrapper);
    }

    /**
     * 新增
     */
    @Override
    public int addAgricultureMaterial(AgricultureMaterial agricultureMaterial) {
        return agricultureMaterialMapper.insert(agricultureMaterial);
    }

    /**
     * 修改农资信息
     * @param agricultureMaterial
     * @return
     */
    @Override
    public int updateagricultureMaterial(AgricultureMaterial agricultureMaterial) {
        return agricultureMaterialMapper.updateById(agricultureMaterial);
    }

    /**
     * 删除
     *
     * @return
     */
    @Override
    public int deleteById(Long materialId) {
        return agricultureMaterialMapper.deleteById(materialId);
    }


    /**
     * 校验
     * @param agricultureMaterial
     */
    private void validate(AgricultureMaterial agricultureMaterial){
        // 创建一个 LambdaQueryWrapper 对象，用于构建查询条件
        LambdaQueryWrapper<AgricultureMaterial> lambda = new QueryWrapper<AgricultureMaterial>().lambda();
        // 添加条件：查询数据库中 materialName 与当前对象的 materialName 相等的记录
        lambda
                .eq(AgricultureMaterial::getMaterialName, agricultureMaterial.getMaterialName());
        // 从数据库中查询符合条件的对象
        AgricultureMaterial info = agricultureMaterialMapper.selectOne(lambda);
//        如果 info 不是空，并且 info 的 materialId 不等于当前的 materialId，抛出异常
        if (!ObjectUtils.isEmpty(info) && info.getMaterialId() != agricultureMaterial.getMaterialId()) {
            throw new ServiceException(AgricultureConstants.CLASS_NAME_EXIST);
        }
    }
}

