package com.server.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureTaskLog;

/**
 * 批次任务日志Service接口
 * 
 * @author server
 * @date 2025-06-11
 */
public interface AgricultureTaskLogService extends IService<AgricultureTaskLog>
{
    /**
     * 查询批次任务日志
     * 
     * @param logId 批次任务日志主键
     * @return 批次任务日志
     */
    public AgricultureTaskLog selectAgricultureTaskLogByLogId(String logId);

    /**
     * 查询批次任务日志列表
     * 
     * @param agricultureTaskLog 批次任务日志
     * @return 批次任务日志集合
     */
    public List<AgricultureTaskLog> selectAgricultureTaskLogList(AgricultureTaskLog agricultureTaskLog);

    /**
     * 新增批次任务日志
     * 
     * @param agricultureTaskLog 批次任务日志
     * @return 结果
     */
    public int insertAgricultureTaskLog(AgricultureTaskLog agricultureTaskLog);

    /**
     * 修改批次任务日志
     * 
     * @param agricultureTaskLog 批次任务日志
     * @return 结果
     */
    public int updateAgricultureTaskLog(AgricultureTaskLog agricultureTaskLog);

    /**
     * 批量删除批次任务日志
     * 
     * @param logIds 需要删除的批次任务日志主键集合
     * @return 结果
     */
    public int deleteAgricultureTaskLogByLogIds(String[] logIds);

    /**
     * 删除批次任务日志信息
     * 
     * @param logId 批次任务日志主键
     * @return 结果
     */
    public int deleteAgricultureTaskLogByLogId(String logId);
}
