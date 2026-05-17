package com.meet.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryFailedEvent {

    private String orderId;
    private String productId;
    private int requestedQuantity;
    private int availableQuantity;
    private String reason;
}
