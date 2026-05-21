package com.meet.inventory_service.repository;

import com.meet.inventory_service.model.InventoryItem;
import com.meet.inventory_service.model.InventoryReservation;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InventoryRepository {

    private final Map<String, InventoryItem> inventory = new HashMap<>();

    @PostConstruct
    public void init() {
        inventory.put("P101", new InventoryItem("P101", 100));
        inventory.put("P102", new InventoryItem("P102", 50));
        inventory.put("P103", new InventoryItem("P103", 30));
    }

    public Optional<InventoryItem> findByProductId(String productId) {
        return Optional.ofNullable(inventory.get(productId));
    }

    public synchronized InventoryReservation reserve(String productId, int requestedQuantity) {
        InventoryItem item = inventory.get(productId);

        if (item == null) {
            return new InventoryReservation(false, requestedQuantity, 0, 0, "Product not found");
        }

        int availableQuantity = item.getAvailableQuantity();

        if (availableQuantity < requestedQuantity) {
            return new InventoryReservation(
                    false,
                    requestedQuantity,
                    availableQuantity,
                    availableQuantity,
                    "Insufficient stock"
            );
        }

        int remainingQuantity = availableQuantity - requestedQuantity;
        item.setAvailableQuantity(remainingQuantity);

        return new InventoryReservation(true, requestedQuantity, availableQuantity, remainingQuantity, null);
    }
}
