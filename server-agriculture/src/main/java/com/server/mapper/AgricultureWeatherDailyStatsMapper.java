package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureWeatherDailyStats;
import org.apache.ibatis.annotations.Mapper;

/**
 * 气象数据日统计Mapper接口
 *
 * @author server
 * @date 2025-07-11
 */
@Mapper
public interface AgricultureWeatherDailyStatsMapper extends BaseMapper<AgricultureWeatherDailyStats> {

}