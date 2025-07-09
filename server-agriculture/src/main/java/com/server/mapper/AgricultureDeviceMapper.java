package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureDevice;
import com.server.domain.vo.AgricultureDeviceVO;
import org.apache.ibatis.annotations.*;

/**
 * 设备信息Mapper接口
 *
 * @author bxwy
 * @date 2025-05-26
 */
@Mapper
public interface AgricultureDeviceMapper extends BaseMapper<AgricultureDevice>
{
    @Select("SELECT d.*, p.name as pasture_name, b.batch_name, t.type_name as device_type_name " +
            "FROM agriculture_device d " +
            "LEFT JOIN agriculture_pasture p ON d.pasture_id = p.id " +
            "LEFT JOIN agriculture_crop_batch b ON d.batch_id = b.batch_id " +
            "LEFT JOIN agriculture_device_type t ON d.device_type_id = t.id " +
            "${ew.customSqlSegment}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "deviceName", column = "device_name"),
            @Result(property = "deviceImage", column = "device_image"),
            // 其他字段映射
            @Result(property = "pastureName", column = "pasture_name"),
            @Result(property = "batchName", column = "batch_name"),
            @Result(property = "deviceTypeName", column = "device_type_name")
    })
    List<AgricultureDeviceVO> selectAgricultureDeviceVOList(@Param("ew") Wrapper<AgricultureDevice> queryWrapper);
}
