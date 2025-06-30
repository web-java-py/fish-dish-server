package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureDevice;
import com.server.domain.AgricultureDeviceType;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备类型Mapper接口
 *
 * @author server
 * @date 2025-06-20
 */
@Mapper
public interface AgricultureDeviceTypeMapper extends BaseMapper<AgricultureDeviceType>
{
}
