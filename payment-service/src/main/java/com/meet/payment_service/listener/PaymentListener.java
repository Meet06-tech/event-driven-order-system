package com.meet.payment_service.listener;

import com.meet.event.InventoryReservedEvent;
import com.meet.event.PaymentCompletedEvent;
import com.meet.event.PaymentFailedEvent;
import com.meet.payment_service.config.RabbitMQConfig;
import com.meet.payment_service.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentListener.class);

    private final PaymentService paymentService;
    private final RabbitTemplate rabbitTemplate;

    public PaymentListener(PaymentService paymentService, RabbitTemplate rabbitTemplate) {
        this.paymentService = paymentService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE)
    public void handlePayment(InventoryReservedEvent event) {
        log.info("Payment requested after inventory reservation: orderId={}, productId={}, reservedQuantity={}",
                event.getOrderId(), event.getProductId(), event.getReservedQuantity());

        if (paymentService.processPayment(event)) {
            publishPaymentCompleted(event);
            return;
        }

        publishPaymentFailed(event, "Payment validation failed");
    }

    private void publishPaymentCompleted(InventoryReservedEvent event) {
        PaymentCompletedEvent completedEvent = new PaymentCompletedEvent(event.getOrderId());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.PAYMENT_COMPLETED_KEY,
                completedEvent
        );

        log.info("Payment completed for orderId={}", event.getOrderId());
    }

    private void publishPaymentFailed(InventoryReservedEvent event, String reason) {
        PaymentFailedEvent failedEvent = new PaymentFailedEvent(event.getOrderId(), reason);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.PAYMENT_FAILED_KEY,
                failedEvent
        );

        log.warn("Payment failed for orderId={}, reason={}", event.getOrderId(), reason);
    }
}
