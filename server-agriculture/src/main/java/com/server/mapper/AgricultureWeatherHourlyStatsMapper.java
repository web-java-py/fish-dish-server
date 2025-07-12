package com.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureWeatherHourlyStats;
import org.apache.ibatis.annotations.Mapper;

/**
 * 气象数据小时统计Mapper接口
 *
 * @author server
 * @date 2025-07-11
 */
@Mapper
public interface AgricultureWeatherHourlyStatsMapper extends BaseMapper<AgricultureWeatherHourlyStats> {

}