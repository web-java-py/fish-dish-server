package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureClass;
import com.server.mapper.AgricultureClassMapper;
import com.server.service.AgricultureClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgricultureClassServiceImpl extends ServiceImpl<AgricultureClassMapper, AgricultureClass> implements AgricultureClassService {

    @Autowired
    private AgricultureClassMapper agricultureClassMapper;

    @Override
    public List<AgricultureClass> selectAgricultureClasList(AgricultureClass agricultureClass) {
        LambdaQueryWrapper<AgricultureClass> lambda = new QueryWrapper<AgricultureClass>().lambda();
        lambda
                .like(AgricultureClass::getClassName, agricultureClass.getClassName())
                .like(AgricultureClass::getClassTypeName, agricultureClass.getClassTypeName());
        return agricultureClassMapper.selectList(lambda);
    }
}
