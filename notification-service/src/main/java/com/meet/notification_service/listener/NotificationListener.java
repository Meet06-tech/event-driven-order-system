package com.meet.notification_service.listener;

import com.meet.order_service.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    @RabbitListener(queues = "notification.queue")
    public void handleNotification(OrderCreatedEvent event){

        System.out.println("Notification Service Triggered");
        System.out.println("Sending notification for Order: " + event.getOrderId());
        System.out.println("Product: " + event.getProductId());

        System.out.println(
                "Order " + event.getOrderId() +
                        " confirmed. Amount: ₹" + event.getTotalAmount()
        );

        // Simulated notification
        System.out.println(" Email/SMS Sent\n");
    }
}