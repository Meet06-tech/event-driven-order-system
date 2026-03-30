package com.meet.inventory_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private String orderId;
    private String productId;
    private int quantity;
}
