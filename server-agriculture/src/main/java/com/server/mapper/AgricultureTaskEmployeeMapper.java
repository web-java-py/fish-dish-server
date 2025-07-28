package com.server.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureTaskEmployee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 批次任务工人Mapper接口
 * 
 * @author server
 * @date 2025-06-10
 */
@Mapper
public interface AgricultureTaskEmployeeMapper extends BaseMapper<AgricultureTaskEmployee> {

    @Select("SELECT te.* , e.employee_name FROM agriculture_task_employee te LEFT JOIN agriculture_employee e ON te.employee_id = e.employee_id WHERE te.task_id = #{taskId}")
    List<AgricultureTaskEmployee> selectGYID(@Param("taskId") Long taskId);
}
