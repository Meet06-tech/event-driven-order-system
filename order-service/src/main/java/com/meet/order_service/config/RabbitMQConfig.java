package com.meet.order_service.config;

import java.util.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE = "order.created.queue";

    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE);

    }
}
