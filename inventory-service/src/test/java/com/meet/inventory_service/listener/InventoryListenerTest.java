package com.meet.inventory_service.listener;

import com.meet.event.InventoryFailedEvent;
import com.meet.event.InventoryReservedEvent;
import com.meet.inventory_service.config.RabbitMQConfig;
import com.meet.inventory_service.event.OrderCreatedEvent;
import com.meet.inventory_service.model.InventoryReservation;
import com.meet.inventory_service.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryListenerTest {

    @Mock
    private InventoryService inventoryService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private InventoryListener listener;

    @Test
    void handleInventoryPublishesInventoryReservedEventWhenStockIsAvailable() {
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "P101", 2, 200.0);
        when(inventoryService.reserveStock(event))
                .thenReturn(new InventoryReservation(true, 2, 100, 98, null));

        listener.handleInventory(event);

        ArgumentCaptor<InventoryReservedEvent> eventCaptor = ArgumentCaptor.forClass(InventoryReservedEvent.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.INVENTORY_RESERVED_KEY),
                eventCaptor.capture()
        );

        assertThat(eventCaptor.getValue().getOrderId()).isEqualTo("ORDER-1");
        assertThat(eventCaptor.getValue().getRemainingQuantity()).isEqualTo(98);
    }

    @Test
    void handleInventoryPublishesInventoryFailedEventWhenStockIsUnavailable() {
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "P101", 200, 20000.0);
        when(inventoryService.reserveStock(event))
                .thenReturn(new InventoryReservation(false, 200, 100, 100, "Insufficient stock"));

        listener.handleInventory(event);

        ArgumentCaptor<InventoryFailedEvent> eventCaptor = ArgumentCaptor.forClass(InventoryFailedEvent.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.INVENTORY_FAILED_KEY),
                eventCaptor.capture()
        );

        assertThat(eventCaptor.getValue().getOrderId()).isEqualTo("ORDER-1");
        assertThat(eventCaptor.getValue().getAvailableQuantity()).isEqualTo(100);
        assertThat(eventCaptor.getValue().getReason()).isEqualTo("Insufficient stock");
    }
}
