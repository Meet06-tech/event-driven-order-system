package com.meet.inventory_service.service;

import com.meet.inventory_service.event.OrderCreatedEvent;
import com.meet.inventory_service.model.InventoryReservation;
import com.meet.inventory_service.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public InventoryReservation reserveStock(OrderCreatedEvent event) {
        return inventoryRepository.reserve(event.getProductId(), event.getQuantity());
    }
}
