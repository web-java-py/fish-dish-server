package com.server.config;

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

    @Bean
    public Queue AiJobQueue() {
        return new Queue(AI_JOB_QUEUE, true);
    }

    @Bean
    public Queue AiReportQueue() {
        return new Queue(AI_REPORT_QUEUE, true);
    }

    @Bean
    public DirectExchange AiExchange() {
        return new DirectExchange(AI_EXCHANGE, true, false);
    }

    @Bean
    public Binding bindingAiJobBinding() {
        return BindingBuilder.bind(AiJobQueue()).to(AiExchange()).with(AI_JOB_KEY);
    }

    @Bean
    public Binding bindingAiReportBinding() {
        return BindingBuilder.bind(AiReportQueue()).to(AiExchange()).with(AI_REPORT_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
