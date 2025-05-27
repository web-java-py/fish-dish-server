package com.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.server.ai.AiService;
import com.server.domain.AgricultureClass;
import com.server.domain.AgricultureJob;
import com.server.mapper.AgricultureJobMapper;
import com.server.service.AgricultureJobService;
import com.server.utils.StringUtils;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AgricultureJobServiceImpl extends ServiceImpl<AgricultureJobMapper, AgricultureJob> implements AgricultureJobService {

    @Autowired
    private AgricultureJobMapper agricultureJobMapper;

    @Autowired
    private AiService aiService;

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
        String prompt = String.format("你是一位经验丰富的农业专家，请提供%s的%s%s种类生长过程中各个阶段的名称（中文）和周期，时间为诞生到销售。",
                agricultureClass.getClassType() == "0" ? "鱼" : "菜", agricultureClass.getClassTypeName(), agricultureClass.getClassName());
        ResponseFormat build = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .rootElement(JsonObjectSchema.builder()
                                .addProperty("species", JsonStringSchema.builder().build())
                                .addProperty("jobs", JsonArraySchema.builder()
                                        .items(JsonObjectSchema.builder()
                                                .addProperty("jobName", JsonStringSchema.builder().build())
                                                .addProperty("cycUnit", JsonIntegerSchema.builder().description("cycUnit 0表示天单位 1表示周单位").build())
                                                .addProperty("jobStart", JsonIntegerSchema.builder().build())
                                                .addProperty("jobFinish", JsonIntegerSchema.builder().build())
                                                .required("jobName", "cycUnit", "jobStart", "jobFinish")
                                                .build())
                                        .build())
                                .required("species", "jobs")
                                .build())
                        .build())
                .build();
        String response = aiService.chat(prompt, build);
        System.out.println(response);
    }
}
