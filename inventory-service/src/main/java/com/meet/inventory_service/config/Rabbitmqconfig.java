package com.meet.inventory_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Rabbitmqconfig {

    public static final String ORDER_QUEUE = "order.created.queue";

    @Bean
    public Queue  orderQueue() {
        return new Queue(ORDER_QUEUE);
    }
}
