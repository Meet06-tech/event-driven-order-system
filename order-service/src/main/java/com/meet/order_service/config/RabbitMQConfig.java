package com.meet.order_service.config;

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

    public static final String ORDER_QUEUE = "order.created.queue";
//    public static final String ORDER_QUEUE1 = "order.created.queue1";

    @Bean
    public DirectExchange exchange(){
        return new DirectExchange("order.exchange");
    }
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue orderQueue(){
        return new Queue(ORDER_QUEUE);
    }

    @Bean
    public Queue inventoryQueue(){
        return new Queue("inventory.queue");
    }

    @Bean
    public Queue paymentQueue(){
        return new Queue("payment.queue");
    }

    @Bean
    public Queue notificationQueue(){
        return new Queue("notification.queue");
    }

    @Bean
    public Binding inventoryBinding(){
        return BindingBuilder
                .bind(inventoryQueue())
                .to(exchange())
                .with("inventory");
    }

    @Bean
    public Binding paymentBinding(){
        return BindingBuilder
                .bind(paymentQueue())
                .to(exchange())
                .with("payment");
    }

    @Bean
    public Binding notificationBinding(){
        return BindingBuilder
                .bind(notificationQueue())
                .to(exchange())
                .with("notification");
    }


}
