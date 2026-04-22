package com.meet.inventory_service.listener;

import com.meet.inventory_service.event.OrderCreatedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InventoryListener {

    private final Map<String, Integer> inventory = new HashMap<>();

    @PostConstruct
    public void init() {
        inventory.put("P101", 100);
        inventory.put("P102", 50);
        inventory.put("P103", 30);
    }

    @RabbitListener(queues = "inventory.queue")
    public void handleInventory(OrderCreatedEvent event){

        String productId = event.getProductId();
        int quantity = event.getQuantity();

        int currentStock = inventory.getOrDefault(productId, 0);

        if (currentStock >= quantity) {

            inventory.put(productId, currentStock - quantity);

            System.out.println("📦 Inventory Updated");
            System.out.println("Product: " + productId);
            System.out.println("Remaining Stock: " + inventory.get(productId));

        } else {
            System.out.println("❌ Not enough stock for: " + productId);
        }

        System.out.println("----------------------------------");
    }
}