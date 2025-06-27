package com.server.config;

import com.server.json.JacksonObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.server.constant.RabbitMQConstant.*;

/**
 * http://localhost:15672
 */
@Configuration
public class RabbitMQConfig {

    private JacksonObjectMapper jacksonObjectMapper = new JacksonObjectMapper();

    @Bean
    public Queue AiJobQueue() {
        return new Queue(AI_JOB_QUEUE, true);
    }

    @Bean
    public Queue AiReportQueue() {
        return new Queue(AI_REPORT_QUEUE, true);
    }

    @Bean
    public DirectExchange AiExChange() {
        return new DirectExchange(AI_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingAiJobBinding() {
        return BindingBuilder.bind(AiJobQueue()).to(AiExChange()).with(AI_JOB_KEY);
    }

    @Bean
    public Binding bindingAiReportBinding() {
        return BindingBuilder.bind(AiReportQueue()).to(AiExChange()).with(AI_REPORT_KEY);
    }

    @Bean
    public Queue FbApQueue() {
        return new Queue(FB_QUEUE,true);
    }

    @Bean
    public FanoutExchange FbApChange(){
        return new FanoutExchange(FB_EXCHANGE,true,false);
    }

    @Bean
    public Binding  bindingFbApBinding(){
        return BindingBuilder.bind(FbApQueue()).to(FbApChange());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter(jacksonObjectMapper));
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter(jacksonObjectMapper));
        return factory;
    }
}
