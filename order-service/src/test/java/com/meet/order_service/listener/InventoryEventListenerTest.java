package com.meet.order_service.listener;

import com.meet.event.InventoryFailedEvent;
import com.meet.event.InventoryReservedEvent;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.service.OrderStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class InventoryEventListenerTest {

    @Mock
    private OrderStatusService orderStatusService;

    @InjectMocks
    private InventoryEventListener listener;

    @Test
    void handleInventoryReservedMarksOrderAsInventoryReserved() {
        InventoryReservedEvent event = new InventoryReservedEvent("ORDER-1", "P101", 2, 98);

        listener.handleInventoryReserved(event);

        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.INVENTORY_RESERVED);
        verifyNoMoreInteractions(orderStatusService);
    }

    @Test
    void handleInventoryFailedMarksOrderAsInventoryFailed() {
        InventoryFailedEvent event = new InventoryFailedEvent("ORDER-1", "P101", 200, 100, "Insufficient stock");

        listener.handleInventoryFailed(event);

        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.INVENTORY_FAILED);
        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.CANCELLED);
        verifyNoMoreInteractions(orderStatusService);
    }
}
