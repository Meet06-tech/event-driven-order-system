package com.meet.inventory_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InventoryReservation {

    private boolean reserved;
    private int requestedQuantity;
    private int availableQuantity;
    private int remainingQuantity;
    private String failureReason;
}
