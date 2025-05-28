package com.server.consumer;

import com.alibaba.fastjson2.JSON;
import com.server.ai.AiResponseFormat;
import com.server.ai.AiService;

import com.server.domain.AgricultureJob;
import com.server.domain.dto.AiJobDTO;
import com.server.domain.vo.AiEntityVO;
import com.server.service.AgricultureJobService;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.server.constant.RabbitMQConstant.*;

@Log
@Component
public class AiConsumer {

    @Autowired
    private AiService aiService;

    @Autowired
    private AgricultureJobService agricultureJobService;

    /**
     * ai作业处理消费者
     * @param aiEntityVO
     */
    @Transactional
    @RabbitListener(queues = AI_JOB_QUEUE)
    public void process(AiEntityVO aiEntityVO) {
        try {
            String response = aiService.chat(aiEntityVO.getPrompt(), AiResponseFormat.jobResponseFormat());
            AiJobDTO aiJobDTO = JSON.parseObject(response, AiJobDTO.class);
            List<AgricultureJob> agricultureJobList = aiJobDTO.getJobs().stream()
                    .peek(job -> job.setClassId(aiEntityVO.getId()))
                    .collect(Collectors.toList());
            agricultureJobService.delAgricultureJobByClassId(aiEntityVO.getId());
            agricultureJobService.addAgricultureJobBatch(agricultureJobList);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
