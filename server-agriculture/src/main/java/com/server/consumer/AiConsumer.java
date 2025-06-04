package com.server.consumer;

import com.alibaba.fastjson2.JSON;
import com.server.ai.AiResponseFormat;
import com.server.ai.AiService;

import com.server.constant.SocketConstant;
import com.server.domain.AgricultureJob;
import com.server.domain.dto.AiJobDTO;
import com.server.domain.vo.AiMessageVO;
import com.server.domain.vo.SocketMessageVO;
import com.server.service.AgricultureJobService;
import com.server.utils.uuid.IdUtils;
import com.server.ws.MqSocketServer;
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

    @Autowired
    private MqSocketServer mqSocketServer;

    /**
     * ai作业处理消费者
     *
     * @param aiMessageVO
     */
    @Transactional
    @RabbitListener(queues = AI_JOB_QUEUE)
    public void process(AiMessageVO aiMessageVO) {
        try {
            String response = aiService.chat(aiMessageVO.getPrompt(), AiResponseFormat.jobResponseFormat());
            AiJobDTO aiJobDTO = JSON.parseObject(response, AiJobDTO.class);
            List<AgricultureJob> agricultureJobList = aiJobDTO.getJobs().stream()
                    .peek(job -> {
                        job.setClassId(aiMessageVO.getId());
                        job.setUpdateBy(aiMessageVO.getCreateBy());
                        job.setCreateBy(aiMessageVO.getCreateBy());
                    })
                    .collect(Collectors.toList());
            agricultureJobService.delAgricultureJobByClassId(aiMessageVO.getId());
            agricultureJobService.addAgricultureJobBatch(agricultureJobList);
            SocketMessageVO socketResponse = SocketMessageVO.builder()
                    .id(IdUtils.fastSimpleUUID())
                    .type(SocketConstant.AI_JOB_KEY)
                    .content("您的AI助手已经完成种类作业流程的处理啦！")
                    .build();
            mqSocketServer.sendToAllClient(JSON.toJSONString(socketResponse));
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
