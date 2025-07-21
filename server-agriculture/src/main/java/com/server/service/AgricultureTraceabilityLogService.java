package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureTraceabilityLog;

import java.util.List;
import java.util.Map;

public interface AgricultureTraceabilityLogService extends IService<AgricultureTraceabilityLog> {

    /**
     * 记录溯源查询
     */
    void recordTraceabilityQuery(String traceCode, String partitionId, String queryIp, String userAgent, String queryType,String foodType);

    /**
     * 获取溯源统计信息
     */
    Map<String, Object> getTraceabilityStats();

    /**
     * 获取每个溯源码的统计信息
     */
    List<Map<String, Object>> getTraceabilityCodeStats();

    /**
     * 获取按分区的统计信息
     */
    List<Map<String, Object>> getPartitionTraceabilityStats();

    /**
     * 根据溯源码查询该溯源码的查询次数
     */
    Long getTraceabilityCountByCode(String traceCode);

    /**
     * 根据溯源码查询该溯源码的详细信息（包括查询次数）
     */
    Map<String, Object> getTraceabilityDetailByCode(String traceCode);

    /**
     * 根据溯源码查询该溯源码的查询记录列表
     */
    List<Map<String, Object>> getTraceabilityLogsByCode(String traceCode);
}