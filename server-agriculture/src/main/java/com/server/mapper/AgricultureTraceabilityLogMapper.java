package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureTraceabilityLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface AgricultureTraceabilityLogMapper extends BaseMapper<AgricultureTraceabilityLog> {

    /**
     * 统计蔬菜和鱼类的溯源总次数
     */
    @Select("SELECT " +
            "SUM(CASE WHEN f.cuisine_weight IS NOT NULL AND f.cuisine_weight > 0 THEN l.query_count ELSE 0 END) as cuisineTotalCount, " +
            "SUM(CASE WHEN f.fish_weight IS NOT NULL AND f.fish_weight > 0 THEN l.query_count ELSE 0 END) as fishTotalCount " +
            "FROM agriculture_partition_food f " +
            "LEFT JOIN (SELECT trace_code, COUNT(*) as query_count FROM agriculture_traceability_log GROUP BY trace_code) l " +
            "ON f.id = l.trace_code")
    Map<String, Object> getTotalTraceabilityCount();

    /**
     * 统计每个溯源码的查询次数
     */
    @Select("SELECT " +
            "f.id as traceCode, " +
            "COALESCE(l.query_count, 0) as traceCount, " +
            "f.name as foodName, " +
            "f.ia_partition_id as partitionId, " +
            "f.cuisine_weight as cuisineWeight, " +
            "f.fish_weight as fishWeight " +
            "FROM agriculture_partition_food f " +
            "LEFT JOIN (SELECT trace_code, COUNT(*) as query_count FROM agriculture_traceability_log GROUP BY trace_code) l " +
            "ON f.id = l.trace_code " +
            "ORDER BY l.query_count DESC")
    List<Map<String, Object>> getTraceabilityCodeStats();

    /**
     * 按分区统计溯源查询次数
     */
    @Select("SELECT " +
            "f.ia_partition_id as partitionId, " +
            "SUM(CASE WHEN f.cuisine_weight IS NOT NULL AND f.cuisine_weight > 0 THEN COALESCE(l.query_count, 0) ELSE 0 END) as cuisineCount, " +
            "SUM(CASE WHEN f.fish_weight IS NOT NULL AND f.fish_weight > 0 THEN COALESCE(l.query_count, 0) ELSE 0 END) as fishCount, " +
            "SUM(COALESCE(l.query_count, 0)) as totalCount " +
            "FROM agriculture_partition_food f " +
            "LEFT JOIN (SELECT trace_code, COUNT(*) as query_count FROM agriculture_traceability_log GROUP BY trace_code) l " +
            "ON f.id = l.trace_code " +
            "GROUP BY f.ia_partition_id " +
            "ORDER BY totalCount DESC")
    List<Map<String, Object>> getPartitionTraceabilityStats();

    /**
     * 根据溯源码查询该溯源码的查询次数
     */
    @Select("SELECT COUNT(*) as traceCount FROM agriculture_traceability_log WHERE trace_code = #{traceCode}")
    Long getTraceabilityCountByCode(@Param("traceCode") String traceCode);

    /**
     * 根据溯源码查询该溯源码的详细信息（包括查询次数）
     */
    @Select("SELECT " +
            "f.id as traceCode, " +
            "f.name as foodName, " +
            "f.ia_partition_id as partitionId, " +
            "f.cuisine_weight as cuisineWeight, " +
            "f.fish_weight as fishWeight, " +
            "f.date as harvestDate, " +
            "COALESCE(l.query_count, 0) as traceCount " +
            "FROM agriculture_partition_food f " +
            "LEFT JOIN (SELECT trace_code, COUNT(*) as query_count FROM agriculture_traceability_log GROUP BY trace_code) l " +
            "ON f.id = l.trace_code " +
            "WHERE f.id = #{traceCode}")
    Map<String, Object> getTraceabilityDetailByCode(@Param("traceCode") String traceCode);

    /**
     * 根据溯源码查询该溯源码的查询记录列表
     */
    @Select("SELECT " +
            "query_time as queryTime, " +
            "query_ip as queryIp, " +
            "user_agent as userAgent, " +
            "query_type as queryType " +
            "FROM agriculture_traceability_log " +
            "WHERE trace_code = #{traceCode} " +
            "ORDER BY query_time DESC")
    List<Map<String, Object>> getTraceabilityLogsByCode(@Param("traceCode") String traceCode);

    /**
     * 工作台溯源统计
     * 年
     * DATE_FORMAT 将字段 query_time 格式化为 YYYY-MM 的字符串，比如 2025-07 count 统计每个月的记录
     * YEAR(query_time)：提取每条记录的查询时间的年份。  YEAR(CURDATE())：获取当前系统时间的年份
     * @return
     */
    @Select("SELECT " +
            "DATE_FORMAT(query_time, '%Y-%m') as time, COUNT(*) as count FROM agriculture_traceability_log " +
            "WHERE YEAR(query_time) = YEAR(CURDATE()) GROUP BY time ORDER BY time")
    List<Map<String, Object>> getTraceTotalByYear();

    /**
     * 工作台溯源统计
     * 月  CURDATE返回当前的日期 DATE_SUB(CURDATE(), INTERVAL 30 DAY) 从当前日期减去30天
     * @return
     */
    @Select("SELECT " +
            "DATE_FORMAT(query_time, '%Y-%m-%d') as time, COUNT(*) as count FROM agriculture_traceability_log " +
            "WHERE query_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) AND query_time <= CURDATE() GROUP BY time ORDER BY time")
    List<Map<String, Object>> getTraceTotalByMonth();

    /**
     * 工作台溯源统计
     * 日
     * @return
     */
    @Select("SELECT " +
            "DATE_FORMAT(query_time, '%Y-%m-%d') as time, COUNT(*) as count FROM agriculture_traceability_log " +
            "WHERE query_time >= DATE_SUB(CURDATE(), INTERVAL 8 DAY) AND query_time <= CURDATE() GROUP BY time ORDER BY time")
    List<Map<String, Object>> getTraceTotalByWeek();
}