package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureTraceabilityLog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: zbb
 * @Date: 2025/7/16 20:05
 */
public interface TraceTotalMapper extends BaseMapper<AgricultureTraceabilityLog> {

    /**
     * 查询2025年和2024年当前月及前四个月（共五个月）的溯源日志数量，按年和月分组
     */
    @Select("SELECT YEAR(query_time) as year, MONTH(query_time) as month, COUNT(*) as totalCount " +
            "FROM agriculture_traceability_log " +
            "WHERE ((YEAR(query_time) = 2025 AND MONTH(query_time) BETWEEN #{startMonth} AND #{endMonth}) " +
            "OR (YEAR(query_time) = 2024 AND MONTH(query_time) BETWEEN #{startMonth} AND #{endMonth})) " +
            "GROUP BY YEAR(query_time), MONTH(query_time) " +
            "ORDER BY year DESC, month DESC")
    List<Map<String, Object>> getTraceTotal(@Param("startMonth") int startMonth, @Param("endMonth") int endMonth);
}
