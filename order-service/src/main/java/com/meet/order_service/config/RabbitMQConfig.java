package com.meet.order_service.config;

import org.springframework.amqp.core.Queue;  
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "order.created.queue";
    public static final String ORDER_QUEUE1 = "order.created.queue1";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE);
    }

}
