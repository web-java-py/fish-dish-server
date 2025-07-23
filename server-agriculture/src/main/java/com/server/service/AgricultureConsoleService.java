package com.server.service;

import com.server.domain.AgricultureTraceabilityLog;
import com.server.domain.vo.AgricultureConsoleVO;

import java.util.List;
import java.util.Map;

public interface AgricultureConsoleService {

    /**
     * 获取农场数据
     *
     * @return
     */
    List<AgricultureConsoleVO> listAgriculture();

    /**
     * 统计数据
     *
     * @return
     */
    List<AgricultureConsoleVO> listBatchTask();


    /**
     * 溯源统计
     * @return
     */
    Map<String, Object> listTraceTotal();


}
