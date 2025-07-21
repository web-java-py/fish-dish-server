package com.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureTraceabilityLog;
import com.server.mapper.AgricultureTraceabilityLogMapper;
import com.server.service.AgricultureTraceabilityLogService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class AgricultureTraceabilityLogServiceImpl extends ServiceImpl<AgricultureTraceabilityLogMapper, AgricultureTraceabilityLog>
        implements AgricultureTraceabilityLogService {

    @Override
    public void recordTraceabilityQuery(String traceCode, String partitionId, String queryIp, String userAgent, String queryType,String foodType) {
        AgricultureTraceabilityLog log = new AgricultureTraceabilityLog();
        log.setTraceCode(traceCode);
        log.setPartitionId(partitionId);
        log.setQueryTime(new Date());
        log.setQueryIp(queryIp);
        log.setUserAgent(userAgent);
        log.setQueryType(queryType);
        log.setFoodType(foodType);
        this.save(log);
    }

    @Override
    public Map<String, Object> getTraceabilityStats() {
        return baseMapper.getTotalTraceabilityCount();
    }

    @Override
    public List<Map<String, Object>> getTraceabilityCodeStats() {
        return baseMapper.getTraceabilityCodeStats();
    }

    @Override
    public List<Map<String, Object>> getPartitionTraceabilityStats() {
        return baseMapper.getPartitionTraceabilityStats();
    }

    @Override
    public Long getTraceabilityCountByCode(String traceCode) {
        return baseMapper.getTraceabilityCountByCode(traceCode);
    }

    @Override
    public Map<String, Object> getTraceabilityDetailByCode(String traceCode) {
        return baseMapper.getTraceabilityDetailByCode(traceCode);
    }

    @Override
    public List<Map<String, Object>> getTraceabilityLogsByCode(String traceCode) {
        return baseMapper.getTraceabilityLogsByCode(traceCode);
    }
}