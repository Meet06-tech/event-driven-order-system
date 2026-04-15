package com.meet.order_service.event;

import lombok.Data;

@Data
public class OrderCreatedEvent {

    private String orderId;
    private String productId;
    private int quantity;
}