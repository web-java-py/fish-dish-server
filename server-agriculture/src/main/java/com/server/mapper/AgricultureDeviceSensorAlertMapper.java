package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceSensorAlert;
import org.apache.ibatis.annotations.Mapper;

/**
 * 传感器预警信息Mapper接口
 *
 * @author server
 * @date 2025-05-26
 */
@Mapper
public interface AgricultureDeviceSensorAlertMapper extends BaseMapper<AgricultureDeviceSensorAlert>
{

}
