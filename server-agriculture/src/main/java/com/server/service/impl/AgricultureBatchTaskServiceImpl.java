package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureBatchTask;
import com.server.mapper.AgricultureBatchTaskMapper;
import com.server.service.AgricultureBatchTaskService;
import com.server.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AgricultureBatchTaskServiceImpl extends ServiceImpl<AgricultureBatchTaskMapper, AgricultureBatchTask> implements AgricultureBatchTaskService {

    @Autowired
    private AgricultureBatchTaskMapper batchTaskServiceMapper;

    /**
     * 查询批次任务列表
     *
     * @param agricultureBatchTask 查询条件
     * @return 批次任务列表
     */
    @Override
    public List<AgricultureBatchTask> selectBatchTaskList(AgricultureBatchTask agricultureBatchTask) {
        // 创建 LambdaQueryWrapper 用于构建动态查询条件
        LambdaQueryWrapper<AgricultureBatchTask> queryWrapper = new LambdaQueryWrapper<>();
        // 获取 planFinish 字段的值（结束日期）
        Date planFinishDate = agricultureBatchTask.getPlanFinish();
        if (planFinishDate != null) {
            // 将 Date 类型转换为 LocalDateTime，便于处理时间部分
            LocalDateTime finishDateTime = planFinishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            // 如果时间为 00:00:00，说明前端只传了日期，需要补全为 23:59:59.999，确保当天的数据能被查出
            if (finishDateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
                finishDateTime = finishDateTime.with(LocalTime.MAX);
                // 将补全后的 LocalDateTime 转回 Date 类型
                planFinishDate = Date.from(finishDateTime.atZone(ZoneId.systemDefault()).toInstant());
            }
        }
        // 如果 batchId 不为空，添加等值查询条件
        queryWrapper.eq(agricultureBatchTask.getBatchId() != null,
                        AgricultureBatchTask::getBatchId,
                        agricultureBatchTask.getBatchId())
                // 如果 taskName 不为空，添加模糊查询条件
                .like(StringUtils.isNotBlank(agricultureBatchTask.getTaskName()),
                        AgricultureBatchTask::getTaskName,
                        agricultureBatchTask.getTaskName())
                // 如果 status 不为空，添加等值查询条件
                .eq(StringUtils.isNotBlank(agricultureBatchTask.getStatus()),
                        AgricultureBatchTask::getStatus,
                        agricultureBatchTask.getStatus())
                // 如果 planStart 不为空，添加大于等于的范围查询条件
                .ge(agricultureBatchTask.getPlanStart() != null, AgricultureBatchTask::getPlanStart, agricultureBatchTask.getPlanStart())
                // 如果 planFinish 不为空，添加小于等于的范围查询条件（已补全为 23:59:59）
                .le(planFinishDate != null, AgricultureBatchTask::getPlanFinish, planFinishDate)
                // 设置结果集按照 orderNum 字段升序排序
                .orderByAsc(AgricultureBatchTask::getOrderNum);
        // 日志输出查询参数，便于调试
        log.info("Query parameters: {}", agricultureBatchTask);
        // 执行查询，获取结果列表
        List<AgricultureBatchTask> result = baseMapper.selectList(queryWrapper);
        // 日志输出结果数量，便于调试
        log.info("Query result size: {}", result.size());
        // 返回查询结果
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