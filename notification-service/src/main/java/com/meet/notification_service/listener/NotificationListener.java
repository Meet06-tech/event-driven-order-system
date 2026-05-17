package com.meet.notification_service.listener;

import com.meet.event.PaymentCompletedEvent;
import com.meet.notification_service.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(PaymentCompletedEvent event) {
        log.info("Sending confirmation notification for paid order: orderId={}", event.getOrderId());
    }
}
