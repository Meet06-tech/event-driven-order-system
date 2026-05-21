package com.meet.order_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "order.exchange";

    public static final String PAYMENT_QUEUE = "payment.request.queue";
    public static final String PAYMENT_COMPLETED_QUEUE = "payment.completed.queue";
    public static final String PAYMENT_FAILED_QUEUE = "payment.failed.queue";
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String INVENTORY_RESERVED_QUEUE = "inventory.reserved.queue";
    public static final String INVENTORY_FAILED_QUEUE = "inventory.failed.queue";
    public static final String NOTIFICATION_QUEUE = "notification.queue";

    public static final String ORDER_CREATED_KEY = "order.created";
    public static final String PAYMENT_COMPLETED_KEY = "payment.completed";
    public static final String PAYMENT_FAILED_KEY = "payment.failed";
    public static final String INVENTORY_RESERVED_KEY = "inventory.reserved";
    public static final String INVENTORY_FAILED_KEY = "inventory.failed";
    public static final String NOTIFICATION_KEY = "notification";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    // Exchange
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    // Queues
    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_QUEUE);
    }

    @Bean
    public Queue paymentCompletedQueue() {
        return new Queue(PAYMENT_COMPLETED_QUEUE);
    }

    @Bean
    public Queue paymentFailedQueue() {
        return new Queue(PAYMENT_FAILED_QUEUE);
    }

    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE);
    }

    @Bean
    public Queue inventoryReservedQueue() {
        return new Queue(INVENTORY_RESERVED_QUEUE);
    }

    @Bean
    public Queue inventoryFailedQueue() {
        return new Queue(INVENTORY_FAILED_QUEUE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    // Bindings
    @Bean
    public Binding paymentBinding(Queue paymentQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentQueue)
                .to(exchange)
                .with(INVENTORY_RESERVED_KEY);
    }

    @Bean
    public Binding paymentCompletedBinding(Queue paymentCompletedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentCompletedQueue)
                .to(exchange)
                .with(PAYMENT_COMPLETED_KEY);
    }

    @Bean
    public Binding paymentFailedBinding(Queue paymentFailedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(paymentFailedQueue)
                .to(exchange)
                .with(PAYMENT_FAILED_KEY);
    }

    @Bean
    public Binding inventoryBinding(Queue inventoryQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryQueue)
                .to(exchange)
                .with(ORDER_CREATED_KEY);
    }

    @Bean
    public Binding inventoryReservedBinding(Queue inventoryReservedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryReservedQueue)
                .to(exchange)
                .with(INVENTORY_RESERVED_KEY);
    }

    @Bean
    public Binding inventoryFailedBinding(Queue inventoryFailedQueue, DirectExchange exchange) {
        return BindingBuilder.bind(inventoryFailedQueue)
                .to(exchange)
                .with(INVENTORY_FAILED_KEY);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange exchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(exchange)
                .with(ORDER_CREATED_KEY);
    }


}
