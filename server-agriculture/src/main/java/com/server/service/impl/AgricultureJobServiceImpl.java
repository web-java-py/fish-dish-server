package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.constant.AgricultureConstants;
import com.server.constant.CacheConstants;
import com.server.constant.Constants;
import com.server.constant.RabbitMQConstant;
import com.server.core.redis.RedisCache;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureJob;
import com.server.domain.vo.AiMessageVO;
import com.server.exception.ServiceException;
import com.server.mapper.AgricultureJobMapper;
import com.server.service.AgricultureJobService;
import com.server.utils.SecurityUtils;
import com.server.utils.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AgricultureJobServiceImpl extends ServiceImpl<AgricultureJobMapper, AgricultureJob> implements AgricultureJobService {

    @Autowired
    private AgricultureJobMapper agricultureJobMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RedisCache redisCache;

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
                .eq(ObjectUtils.isNotEmpty(agricultureJob.getClassId()), AgricultureJob::getClassId, agricultureJob.getClassId())
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
     *
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

    /**
     * ai作业数据
     *
     * @param agricultureClass
     */
    @Override
    public void aiAddAgricultureJob(AgricultureClass agricultureClass) {
        String key = CacheConstants.AI_JOB_NOT_REPEAT_SUBMIT + agricultureClass.getClassId();
        if (StringUtils.isNotEmpty((String) redisCache.getCacheObject(key))){
            throw new ServiceException(Constants.AI_NOT_REPEAT_SUBMIT_MESSAGE);
        }
        String prompt = String.format("你是一位经验丰富的农业专家，请提供%s%s种类生长过程中各个阶段的名称（中文）和周期。cycUnit 0表示天单位 1表示周单位",
                agricultureClass.getClassTypeName(), agricultureClass.getClassName());
        AiMessageVO aiMessageVO = AiMessageVO.builder()
                .id(agricultureClass.getClassId())
                .prompt(prompt)
                .createBy(SecurityUtils.getUsername())
                .build();
        rabbitTemplate.convertAndSend(RabbitMQConstant.AI_EXCHANGE, RabbitMQConstant.AI_JOB_KEY, aiMessageVO);
        redisCache.setCacheObject(key, agricultureClass.getClassName(), Constants.AI_NOT_REPEAT_SUBMIT_EXPIRATION, TimeUnit.MINUTES);
    }

    /**
     * 新增作业数据集合
     */
    @Override
    public int addAgricultureJobBatch(List<AgricultureJob> agricultureJobList) {
        return this.saveBatch(agricultureJobList) ? 1 : 0;
    }

    /**
     * 根据classId删除作业数据
     */
    @Override
    public int delAgricultureJobByClassId(Long classId) {
        LambdaQueryWrapper<AgricultureJob> lambda = new QueryWrapper<AgricultureJob>().lambda();
        lambda
                .eq(ObjectUtils.isNotEmpty(classId), AgricultureJob::getClassId, classId);
        return agricultureJobMapper.delete(lambda);
    }
}
