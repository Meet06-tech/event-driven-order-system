package com.meet.inventory_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryItem {

    private String productId;
    private int availableQuantity;
}
