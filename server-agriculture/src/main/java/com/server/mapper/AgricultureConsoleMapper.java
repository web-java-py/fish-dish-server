package com.server.mapper;

import com.server.domain.vo.TraceTotalVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

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
}
