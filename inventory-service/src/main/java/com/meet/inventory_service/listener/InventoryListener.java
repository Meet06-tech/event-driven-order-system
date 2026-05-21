package com.meet.inventory_service.listener;

import com.meet.event.InventoryFailedEvent;
import com.meet.event.InventoryReservedEvent;
import com.meet.inventory_service.config.RabbitMQConfig;
import com.meet.inventory_service.event.OrderCreatedEvent;
import com.meet.inventory_service.model.InventoryReservation;
import com.meet.inventory_service.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {

    private static final Logger log = LoggerFactory.getLogger(InventoryListener.class);

    private final InventoryService inventoryService;
    private final RabbitTemplate rabbitTemplate;

    public InventoryListener(InventoryService inventoryService, RabbitTemplate rabbitTemplate) {
        this.inventoryService = inventoryService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE)
    public void handleInventory(OrderCreatedEvent event) {
        log.info("Inventory reservation requested for orderId={}, productId={}, quantity={}",
                event.getOrderId(), event.getProductId(), event.getQuantity());

        InventoryReservation reservation = inventoryService.reserveStock(event);

        if (reservation.isReserved()) {
            publishInventoryReserved(event, reservation);
            return;
        }

        publishInventoryFailed(event, reservation);
    }

    private void publishInventoryReserved(OrderCreatedEvent event, InventoryReservation reservation) {
        InventoryReservedEvent reservedEvent = new InventoryReservedEvent(
                event.getOrderId(),
                event.getProductId(),
                event.getQuantity(),
                reservation.getRemainingQuantity()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.INVENTORY_RESERVED_KEY,
                reservedEvent
        );

        log.info("Inventory reserved for orderId={}, productId={}, remainingQuantity={}",
                event.getOrderId(), event.getProductId(), reservation.getRemainingQuantity());
    }

    private void publishInventoryFailed(OrderCreatedEvent event, InventoryReservation reservation) {
        InventoryFailedEvent failedEvent = new InventoryFailedEvent(
                event.getOrderId(),
                event.getProductId(),
                event.getQuantity(),
                reservation.getAvailableQuantity(),
                reservation.getFailureReason()
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.INVENTORY_FAILED_KEY,
                failedEvent
        );

        log.warn("Inventory reservation failed for orderId={}, productId={}, reason={}",
                event.getOrderId(), event.getProductId(), reservation.getFailureReason());
    }
}
