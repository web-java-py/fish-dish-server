package com.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureClass;
import com.server.mapper.AgricultureClassMapper;
import com.server.service.AgricultureClassService;
import org.springframework.stereotype.Service;

@Service
public class AgricultureClassServiceImpl extends ServiceImpl<AgricultureClassMapper, AgricultureClass> implements AgricultureClassService {
}
