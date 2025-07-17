package com.server.service.impl;

// 引入相关依赖
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureTraceabilityLog;
import com.server.mapper.TraceTotalMapper;
import com.server.service.TraceTotalService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @Author: zbb
 * @Date: 2025/7/16 20:03
 */
@Service
public class TraceTotalServiceImpl extends ServiceImpl<TraceTotalMapper, AgricultureTraceabilityLog> implements TraceTotalService {

    /**
     * 获取近5个月的溯源总量数据
     * 该方法会根据当前月份，计算出起始月份和结束月份，然后调用Mapper层方法查询对应区间的溯源总量。
     *
     * @return List<Map<String, Object>> 返回每个月的溯源总量数据，key为字段名，value为对应值
     */
    @Override
    public List<Map<String, Object>> getTraceTotal() {
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 计算结束月份（当前月）
        int endMonth = now.getMonthValue();
        // 计算起始月份（往前推4个月，最小为1月）
        int startMonth = endMonth - 4 > 0 ? endMonth - 4 : 1;
        // 调用Mapper方法查询区间内的溯源总量
        return baseMapper.getTraceTotal(startMonth, endMonth);
    }
}
