package com.meet.inventory_service.service;

import com.meet.inventory_service.event.OrderCreatedEvent;
import com.meet.inventory_service.model.InventoryReservation;
import com.meet.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryServiceTest {

    private final InventoryRepository inventoryRepository = new InventoryRepository();
    private final InventoryService inventoryService = new InventoryService(inventoryRepository);

    @Test
    void reserveStockReservesWhenEnoughStockIsAvailable() {
        inventoryRepository.init();
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "P101", 2, 200.0);

        InventoryReservation reservation = inventoryService.reserveStock(event);

        assertThat(reservation.isReserved()).isTrue();
        assertThat(reservation.getRemainingQuantity()).isEqualTo(98);
    }

    @Test
    void reserveStockFailsWhenStockIsInsufficient() {
        inventoryRepository.init();
        OrderCreatedEvent event = new OrderCreatedEvent("ORDER-1", "U101", "P103", 100, 10000.0);

        InventoryReservation reservation = inventoryService.reserveStock(event);

        assertThat(reservation.isReserved()).isFalse();
        assertThat(reservation.getAvailableQuantity()).isEqualTo(30);
        assertThat(reservation.getFailureReason()).isEqualTo("Insufficient stock");
    }
}
