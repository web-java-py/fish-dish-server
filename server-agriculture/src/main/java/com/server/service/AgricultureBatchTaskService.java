package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureBatchTask;

import java.util.List;

/**
 * @Author: zbb
 * @Date: 2025/5/28 22:22
 */
public interface AgricultureBatchTaskService extends IService<AgricultureBatchTask> {

    /**
     * 查询批次任务列表
     *
     * @param
     * @return 批次任务集合
     */
    List<AgricultureBatchTask> selectBatchTaskList(AgricultureBatchTask agricultureBatchTask);

    /**
     * 查询批次任务
     *
     * @param taskId 批次任务主键
     * @return 批次任务
     */
    AgricultureBatchTask selectBatchTaskByTaskId(Long taskId);


    /**
     * 删除
     * @param taskId
     * @return
     */
    int deleteAgricultureCropBatchByBatchIds(Long taskId);

    /**
     * 修改批次任务
     * @param agricultureBatchTask
     * @return
     */
    int updateBatchTask(AgricultureBatchTask agricultureBatchTask);

    /**
     * 新增批次任务
     * @param agricultureBatchTask
     * @return
     */
    int insertBatchTask(AgricultureBatchTask agricultureBatchTask);
}
