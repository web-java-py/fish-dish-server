package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.domain.AgricultureClass;
import com.server.exception.ServiceException;
import com.server.mapper.AgricultureClassMapper;
import com.server.service.AgricultureClassService;
import com.server.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgricultureClassServiceImpl extends ServiceImpl<AgricultureClassMapper, AgricultureClass> implements AgricultureClassService {

    @Autowired
    private AgricultureClassMapper agricultureClassMapper;

    /**
     * 查询种类数据
     *
     * @param agricultureClass
     * @return
     */
    @Override
    public List<AgricultureClass> selectAgricultureClassList(AgricultureClass agricultureClass) {
        LambdaQueryWrapper<AgricultureClass> lambda = new QueryWrapper<AgricultureClass>().lambda();
        lambda
                .like(StringUtils.isNotEmpty(agricultureClass.getClassName()), AgricultureClass::getClassName, agricultureClass.getClassName())
                .like(StringUtils.isNotEmpty(agricultureClass.getClassName()), AgricultureClass::getClassTypeName, agricultureClass.getClassTypeName());
        return agricultureClassMapper.selectList(lambda);
    }

    /**
     * 新增种类数据
     *
     * @param agricultureClass
     * @return
     */
    @Override
    public int addAgricultureClass(AgricultureClass agricultureClass) {
        validate(agricultureClass);
        return agricultureClassMapper.insert(agricultureClass);
    }

    /**
     * 修改种类数据
     * @param agricultureClass
     * @return
     */
    @Override
    public int editAgricultureClass(AgricultureClass agricultureClass) {
        validate(agricultureClass);
        return agricultureClassMapper.updateById(agricultureClass);
    }

    /**
     * 删除种类数据
     * @param classId
     * @return
     */
    @Override
    public int delAgricultureClass(Long classId) {
        return agricultureClassMapper.deleteById(classId);
    }

    /**
     * 校验
     * @param agricultureClass
     */
    private void validate(AgricultureClass agricultureClass){
        LambdaQueryWrapper<AgricultureClass> lambda = new QueryWrapper<AgricultureClass>().lambda();
        lambda
                .eq(AgricultureClass::getClassName, agricultureClass.getClassName());
        AgricultureClass info = agricultureClassMapper.selectOne(lambda);
        if (!ObjectUtils.isEmpty(info) && info.getClassId() != agricultureClass.getClassId()) {
            throw new ServiceException(AgricultureConstants.CLASS_NAME_EXIST);
        }
    }
}
