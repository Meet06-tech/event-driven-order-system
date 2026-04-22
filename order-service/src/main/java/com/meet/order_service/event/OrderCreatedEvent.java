package com.meet.order_service.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatedEvent {

    private String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private double totalAmount;


    public OrderCreatedEvent(String id, String productId, int quantity, double totalAmount) {
    }
}
