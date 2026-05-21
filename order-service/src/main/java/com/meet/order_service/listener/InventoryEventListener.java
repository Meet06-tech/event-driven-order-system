package com.meet.order_service.listener;

import com.meet.event.InventoryFailedEvent;
import com.meet.event.InventoryReservedEvent;
import com.meet.order_service.config.RabbitMQConfig;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.service.OrderStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryEventListener.class);

    private final OrderStatusService orderStatusService;

    public InventoryEventListener(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_RESERVED_QUEUE)
    public void handleInventoryReserved(InventoryReservedEvent event) {
        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.INVENTORY_RESERVED);

        log.info("Order marked as INVENTORY_RESERVED: orderId={}, productId={}, reservedQuantity={}",
                event.getOrderId(), event.getProductId(), event.getReservedQuantity());
    }

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_FAILED_QUEUE)
    public void handleInventoryFailed(InventoryFailedEvent event) {
        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.INVENTORY_FAILED);
        orderStatusService.updateStatus(event.getOrderId(), OrderStatus.CANCELLED);

        log.warn("Order marked as INVENTORY_FAILED and CANCELLED: orderId={}, productId={}, reason={}",
                event.getOrderId(), event.getProductId(), event.getReason());
    }
}
