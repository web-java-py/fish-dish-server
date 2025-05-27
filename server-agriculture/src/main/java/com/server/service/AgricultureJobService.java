package com.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.server.domain.AgricultureJob;

import java.util.List;

public interface AgricultureJobService extends IService<AgricultureJob> {

    /**
     * 查询作业数据
     * @param agricultureJob
     * @return
     */
    List<AgricultureJob> selectAgricultureJobList(AgricultureJob agricultureJob);

    /**
     * 新增作业数据
     * @param agricultureJob
     * @return
     */
    int addAgricultureJob(AgricultureJob agricultureJob);

    /**
     * 修改作业数据
     * @param agricultureJob
     * @return
     */
    int editAgricultureJob(AgricultureJob agricultureJob);

    /**
     * 删除作业数据
     * @param jobId
     * @return
     */
    int delAgricultureJob(Long jobId);
}
