package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureCropBatch;
import com.server.domain.dto.AgricultureCropBatchDTO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

/**
 * 分区Mapper接口
 *
 * @author server
 * @date 2025-05-28
 */
@Repository
public interface AgricultureCropBatchMapper  extends BaseMapper<AgricultureCropBatch>
{
    /**
     * 根据条件查询作物批次信息，并包含相关的分类图片
     *
     * @param queryWrapper 查询条件Wrapper，用于动态拼接SQL
     * @return 符合条件的作物批次DTO列表
     */
    @Select("SELECT b.*, c.class_image,c.class_name,u.nick_name " +
            "FROM agriculture_crop_batch b " +
            "LEFT JOIN agriculture_class c ON b.germplasm_id = c.class_id OR b.batch_name = c.class_name " +
            "LEFT JOIN agriculture_class c1 ON b.vegetable_id = c1.class_id OR b.batch_name = c1.class_name " +
            "LEFT JOIN sys_user u on u.user_id=b.responsible_person_id " +
            "${ew.customSqlSegment}")
    /**
     * ew 是传入的查询包装器（Wrapper），它可以根据传入的条件动态构建 SQL 片段
     * @Param(Constants.WRAPPER) 这个注解用于将 queryWrapper 参数绑定到动态 SQL 中 Constants.WRAPPER 是 MyBatis-Plus 中一个定义好的常量，用于标识查询条件
     */
    List<AgricultureCropBatchDTO> selectCropBatchWithClassImages(@Param(Constants.WRAPPER) Wrapper<AgricultureCropBatch> queryWrapper);
}
