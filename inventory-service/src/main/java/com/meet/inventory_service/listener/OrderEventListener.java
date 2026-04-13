package com.meet.inventory_service.listener;

import com.meet.inventory_service.config.RabbitMQConfig;
import com.meet.inventory_service.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    @RabbitListener(queues = "notification.queue")
    public void handleOrderCreated(OrderCreatedEvent event){

        System.out.println("Inventory Service Received Event");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Product: " + event.getProductId());
        System.out.println("Quantity: " + event.getQuantity());
    }
}
