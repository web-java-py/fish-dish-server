package com.server.consumer;

import com.server.ai.AiResponseFormat;
import com.server.ai.AiService;

import com.server.domain.AiEntity;
import lombok.extern.java.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.server.constant.RabbitMQConstant.*;

@Log
@Component
public class AiConsumer {

    @Autowired
    private AiService aiService;

    @RabbitListener(queues = AI_JOB_QUEUE)
    public void process(AiEntity aiEntity) {
        try {
            String response = aiService.chat(aiEntity.getPrompt(), AiResponseFormat.jobResponseFormat());
            System.out.println(response);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
    }
}
