package com.server.mapper;

import com.server.domain.vo.TraceTotalVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface AgricultureConsoleMapper {
    /**
     * 溯源统计
     *
     * @return
     */
    @Select("<script>"
            + "SELECT DATE_FORMAT(query_time, '%Y-%m-%d') AS date, COUNT(*) AS count "
            + "FROM agriculture_traceability_log "
            + "WHERE DATE_FORMAT(query_time, '%Y-%m-%d') IN "
            + "<foreach item='date' index='index' collection='dates' "
            + "separator=',' open='(' close=')'>#{date}</foreach> "
            + "GROUP BY DATE_FORMAT(query_time, '%Y-%m-%d') "
            + "ORDER BY DATE_FORMAT(query_time, '%Y-%m-%d') DESC"
            + "</script>")
    List<TraceTotalVO> listTraceTotal(@Param("dates") List<String> dates);

    /**
     * 工作台溯源统计
     * 年
     * DATE_FORMAT 将字段 query_time 格式化为 YYYY-MM 的字符串，比如 2025-07 count 统计每个月的记录
     * DATE_FORMAT(..., '%Y-%m-01') 将当前日期格式化为当前月份的第一天
     * LAST_DAY() 函数返回给定日期所在月份的最后一天
     *
     * @return
     */
    @Select("SELECT DATE_FORMAT(query_time, '%Y-%m') AS time, " +
            "SUM(CASE WHEN food_type = 'fish' THEN 1 ELSE 0 END) AS fishCount, " +
            "SUM(CASE WHEN food_type = 'cuisine' THEN 1 ELSE 0 END) AS cuisineCount " +
            "FROM agriculture_traceability_log " +
            "WHERE query_time >= DATE_SUB(DATE_FORMAT(CURDATE(), '%Y-%m-01'), INTERVAL 11 MONTH) AND query_time <= LAST_DAY(CURDATE()) " +
            "GROUP BY time ORDER BY time")
    List<Map<String, Object>> getTraceTotalByYear();

    /**
     * 工作台溯源统计
     * 月  CURDATE返回当前的日期 DATE_SUB(CURDATE(), INTERVAL 30 DAY) 从当前日期减去30天
     * @return
     */
    @Select("SELECT DATE_FORMAT(query_time, '%Y-%m-%d') AS time, " +
            "SUM(CASE WHEN food_type = 'fish' THEN 1 ELSE 0 END) AS fishCount, " +
            "SUM(CASE WHEN food_type = 'cuisine' THEN 1 ELSE 0 END) AS cuisineCount " +
            "FROM agriculture_traceability_log " +
            "WHERE query_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)AND query_time < DATE_ADD(CURDATE(), INTERVAL 1 DAY) " +
            "GROUP BY time ORDER BY time")
    List<Map<String, Object>> getTraceTotalByMonth();

    /**
     * 工作台溯源统计
     * 周
     * @return
     */
    @Select("SELECT DATE_FORMAT(query_time, '%Y-%m-%d') AS time, " +
            "SUM(CASE WHEN food_type = 'fish' THEN 1 ELSE 0 END) AS fishCount, " +
            "SUM(CASE WHEN food_type = 'cuisine' THEN 1 ELSE 0 END) AS cuisineCount " +
            "FROM agriculture_traceability_log " +
            "WHERE query_time >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)  AND query_time < DATE_ADD(CURDATE(), INTERVAL 1 DAY) " +
            "GROUP BY time ORDER BY time")
    List<Map<String, Object>> getTraceTotalByWeek();


    // 查询近7天（本周）的溯源总数
    @Select("SELECT COUNT(*) FROM agriculture_traceability_log WHERE query_time >= DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    Long getTraceCountThisWeek();

    // 查询上周（前7天）的溯源总数
    @Select("SELECT COUNT(*) FROM agriculture_traceability_log WHERE query_time >= DATE_SUB(CURDATE(), INTERVAL 14 DAY) AND query_time < DATE_SUB(CURDATE(), INTERVAL 7 DAY)")
    Long getTraceCountLastWeek();

    // 本年
    @Select("SELECT COUNT(*) FROM agriculture_traceability_log WHERE YEAR(query_time) = YEAR(CURDATE())")
    Long getTraceCountThisYear();
    // 去年
    @Select("SELECT COUNT(*) FROM agriculture_traceability_log WHERE YEAR(query_time) = YEAR(CURDATE()) - 1")
    Long getTraceCountLastYear();
    // 本月
    @Select("SELECT COUNT(*) FROM agriculture_traceability_log WHERE YEAR(query_time) = YEAR(CURDATE()) AND MONTH(query_time) = MONTH(CURDATE())")
    Long getTraceCountThisMonth();
    // 上月
    @Select("SELECT COUNT(*) FROM agriculture_traceability_log WHERE YEAR(query_time) = YEAR(CURDATE()) AND MONTH(query_time) = MONTH(CURDATE()) - 1 " +
            "OR (MONTH(CURDATE()) = 1 AND YEAR(query_time) = YEAR(CURDATE()) - 1 AND MONTH(query_time) = 12)")
    Long getTraceCountLastMonth();
}
