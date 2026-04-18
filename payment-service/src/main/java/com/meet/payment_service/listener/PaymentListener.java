package com.meet.payment_service.listener;

import com.meet.order_service.event.OrderCreatedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    @RabbitListener(queues = "payment.queue")
    public void handlePayment(OrderCreatedEvent event){

        System.out.println("Payment Service Triggered");
        System.out.println("Order ID: " + event.getOrderId());
        System.out.println("Processing payment for quantity: " + event.getQuantity());
        System.out.println("Processing payment: ₹" + event.getTotalAmount());

        // Simulated logic
        System.out.println("Payment Completed\n");
    }
}