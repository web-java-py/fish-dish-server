package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureClass;

import java.util.List;


public interface AgricultureClassService extends IService<AgricultureClass> {
    
    List<AgricultureClass> selectAgricultureClasList(AgricultureClass agricultureClass);
}
