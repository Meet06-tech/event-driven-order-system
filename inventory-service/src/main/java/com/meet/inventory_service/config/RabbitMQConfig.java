package com.meet.inventory_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "order.exchange";
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String PAYMENT_QUEUE = "payment.request.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String ORDER_CREATED_KEY = "order.created";
    public static final String INVENTORY_RESERVED_KEY = "inventory.reserved";
    public static final String INVENTORY_FAILED_KEY = "inventory.failed";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE);
    }

    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryQueue)
                .to(exchange)
                .with(ORDER_CREATED_KEY);
    }

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }
}
