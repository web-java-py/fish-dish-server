package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureDevice;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备信息Mapper接口
 *
 * @author bxwy
 * @date 2025-05-26
 */
@Mapper
public interface AgricultureDeviceMapper extends BaseMapper<AgricultureDevice>
{

}
