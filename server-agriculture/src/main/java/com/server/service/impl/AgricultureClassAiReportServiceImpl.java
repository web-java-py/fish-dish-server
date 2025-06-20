package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureClassAiReport;
import com.server.mapper.AgricultureClassAiReportMapper;
import com.server.service.AgricultureClassAiReportService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgricultureClassAiReportServiceImpl extends ServiceImpl<AgricultureClassAiReportMapper, AgricultureClassAiReport> implements AgricultureClassAiReportService {

    @Autowired
    private AgricultureClassAiReportMapper agricultureClassAiReportMapper;

    /**
     * 获取种类报告详细信息
     *
     * @param agricultureClassAiReport
     * @return
     */
    @Override
    public AgricultureClassAiReport getAgricultureClassAiReportInfo(AgricultureClassAiReport agricultureClassAiReport) {
        LambdaQueryWrapper<AgricultureClassAiReport> lambda = new QueryWrapper<AgricultureClassAiReport>().lambda();
        lambda
                .eq(ObjectUtils.isNotEmpty(agricultureClassAiReport.getClassId()), AgricultureClassAiReport::getClassId, agricultureClassAiReport.getClassId())
                .orderByDesc(AgricultureClassAiReport::getCreateTime)
                .last("limit 1");
        return agricultureClassAiReportMapper.selectOne(lambda);
    }
}
