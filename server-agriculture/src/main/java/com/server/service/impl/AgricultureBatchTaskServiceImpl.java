package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureBatchTask;
import com.server.mapper.AgricultureBatchTaskServiceMapper;
import com.server.service.AgricultureBatchTaskService;
import com.server.utils.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AgricultureBatchTaskServiceImpl extends ServiceImpl<AgricultureBatchTaskServiceMapper, AgricultureBatchTask> implements AgricultureBatchTaskService {

    @Autowired
    private AgricultureBatchTaskServiceMapper batchTaskServiceMapper;

    /**
     * 查询批次任务列表
     *
     * @param agricultureBatchTask 查询条件
     * @return 批次任务列表
     */
    @Override
    public List<AgricultureBatchTask> selectBatchTaskList(AgricultureBatchTask agricultureBatchTask) {
        LambdaQueryWrapper<AgricultureBatchTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(agricultureBatchTask.getBatchId() != null,
                        AgricultureBatchTask::getBatchId,
                        agricultureBatchTask.getBatchId())
                //只有在 taskName 字段不为空时,这个条件才会被添加。
                .like(StringUtils.isNotBlank(agricultureBatchTask.getTaskName()),
                        AgricultureBatchTask::getTaskName,
                        agricultureBatchTask.getTaskName())
                .eq(StringUtils.isNotBlank(agricultureBatchTask.getStatus()),
                        AgricultureBatchTask::getStatus,
                        agricultureBatchTask.getStatus())
                //设置结果集按照 orderNum 字段升序排序
                .orderByAsc(AgricultureBatchTask::getOrderNum);
        // 添加日志输出，方便调试
        log.info("Query parameters: {}", agricultureBatchTask);
        List<AgricultureBatchTask> result = baseMapper.selectList(queryWrapper);
        log.info("Query result size: {}", result.size());
        return result;
    }

    /**
     * 查询批次任务
     *
     * @param taskId 批次任务主键
     * @return 批次任务
     */
    @Override
    public AgricultureBatchTask selectBatchTaskByTaskId(Long taskId) {
        return getById(taskId);
    }

    /**
     * 删除批次任务
     *
     * @param taskId 批次任务主键
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteAgricultureCropBatchByBatchIds(Long taskId) {
        return removeById(taskId) ? 1 : 0;
    }

    /**
     * 修改批次任务
     *
     * @param agricultureBatchTask 批次任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateBatchTask(AgricultureBatchTask agricultureBatchTask) {
        agricultureBatchTask.setUpdateTime(new Date());
        return updateById(agricultureBatchTask) ? 1 : 0;
    }

    /**
     * 新增批次任务
     *
     * @param agricultureBatchTask 批次任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertBatchTask(AgricultureBatchTask agricultureBatchTask) {
        agricultureBatchTask.setCreateTime(new Date());
        agricultureBatchTask.setStatus("0"); // 默认未分配状态
        return save(agricultureBatchTask) ? 1 : 0;
    }
}