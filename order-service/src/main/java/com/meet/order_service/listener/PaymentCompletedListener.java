package com.meet.order_service.listener;

import com.meet.event.PaymentCompletedEvent;
import com.meet.event.PaymentFailedEvent;
import com.meet.order_service.config.RabbitMQConfig;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.service.OrderStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentCompletedListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentCompletedListener.class);

    private final OrderStatusService orderStatusService;

    public PaymentCompletedListener(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentCompleted(PaymentCompletedEvent event){

        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.PAYMENT_COMPLETED);
        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.CONFIRMED);

        log.info("Order marked as PAYMENT_COMPLETED and CONFIRMED: orderId={}", event.getOrderId());
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailed(PaymentFailedEvent event) {
        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.PAYMENT_FAILED);
        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.CANCELLED);

        log.warn("Order marked as PAYMENT_FAILED and CANCELLED: orderId={}, reason={}",
                event.getOrderId(), event.getReason());
    }
}
