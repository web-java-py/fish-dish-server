package com.server.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: zbb
 * @Date: 2025/7/16 20:02
 */
public interface TraceTotalService {

    /**
     * 查询2025年和2024年当前月及前四个月（共五个月）的溯源日志数量，按年和月分组
     */
    List<Map<String, Object>> getTraceTotal();
}
