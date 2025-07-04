package com.server.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.server.domain.AgricultureBatchTask;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/28 22:24
 */
@Repository
public interface AgricultureBatchTaskServiceMapper extends BaseMapper<AgricultureBatchTask> {

}
