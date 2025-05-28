package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.AgricultureCropBatchDTO;
import com.server.domain.AgricultureDeviceSensorAlert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 分区Mapper接口
 * 
 * @author server
 * @date 2025-05-28
 */
@Repository
public interface AgricultureCropBatchMapper  extends BaseMapper<AgricultureCropBatch>
{
    @Select("SELECT b.*, c.class_image " +
            "FROM agriculture_crop_batch b " +
            "LEFT JOIN agriculture_class c ON b.germplasm_id = c.class_id")
    List<AgricultureCropBatchDTO> selectCropBatchWithClassImages();
}
