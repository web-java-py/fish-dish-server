package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureClassAiReport;

public interface AgricultureClassAiReportService extends IService<AgricultureClassAiReport> {

    /**
     * 获取种类报告详细信息
     * @param agricultureClassAiReport
     * @return
     */
    AgricultureClassAiReport getAgricultureClassAiReportInfo(AgricultureClassAiReport agricultureClassAiReport);
}
