package com.meet.order_service.dto;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private String userId;
    private String productId;
    private int quantity;
}
