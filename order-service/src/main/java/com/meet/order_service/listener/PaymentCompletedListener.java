package com.meet.order_service.listener;

import com.meet.order_service.event.PaymentCompletedEvent;
import com.meet.order_service.model.Order;
import com.meet.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedListener {

    private final OrderRepository orderRepository;

    public PaymentCompletedListener(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @RabbitListener(queues = "payment.completed.queue")
    public void handlePaymentCompleted(PaymentCompletedEvent event){

        Order order = orderRepository.findById(event.getOrderId()).orElseThrow();

        order.setStatus("PAID");
        orderRepository.save(order);

        System.out.println("🟢 Order marked as PAID: " + event.getOrderId());
    }
}