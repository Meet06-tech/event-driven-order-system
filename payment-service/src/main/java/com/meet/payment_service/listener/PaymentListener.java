package com.meet.payment_service.listener;

import com.meet.order_service.event.OrderCreatedEvent;
import com.meet.event.PaymentCompletedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    private final RabbitTemplate rabbitTemplate;

    public PaymentListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "payment.queue")
    public void handlePayment(OrderCreatedEvent event){

        try {
            System.out.println("💰 Payment Service Triggered");
            System.out.println("Order ID: " + event.getOrderId());
            System.out.println("Processing payment: ₹" + event.getTotalAmount());

            System.out.println("✅ Payment Completed\n");

            // 🔥 SEND NEW EVENT
            rabbitTemplate.convertAndSend(
                    "order.exchange",
                    "payment.completed",
                    new PaymentCompletedEvent(event.getOrderId())
            );

        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
        }
    }
}