package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.domain.AgricultureJob;
import com.server.mapper.AgricultureJobMapper;
import com.server.service.AgricultureJobService;
import com.server.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgricultureJobServiceImpl extends ServiceImpl<AgricultureJobMapper, AgricultureJob> implements AgricultureJobService {

    @Autowired
    private AgricultureJobMapper agricultureJobMapper;

    /**
     * 查询作业数据
     *
     * @param agricultureJob
     * @return
     */
    @Override
    public List<AgricultureJob> selectAgricultureJobList(AgricultureJob agricultureJob) {
        LambdaQueryWrapper<AgricultureJob> lambda = new QueryWrapper<AgricultureJob>().lambda();
        lambda
                .like(StringUtils.isNotEmpty(agricultureJob.getJobName()), AgricultureJob::getJobName, agricultureJob.getJobName());
        return agricultureJobMapper.selectList(lambda);
    }

    /**
     * 新增作业数据
     *
     * @param agricultureJob
     * @return
     */
    @Override
    public int addAgricultureJob(AgricultureJob agricultureJob) {
        return agricultureJobMapper.insert(agricultureJob);
    }

    /**
     * 修改作业数据
     * @param agricultureJob
     * @return
     */
    @Override
    public int editAgricultureJob(AgricultureJob agricultureJob) {
        return agricultureJobMapper.updateById(agricultureJob);
    }

    /**
     * 删除作业数据
     *
     * @param jobId
     * @return
     */
    @Override
    public int delAgricultureJob(Long jobId) {
        return agricultureJobMapper.deleteById(jobId);
    }
}
