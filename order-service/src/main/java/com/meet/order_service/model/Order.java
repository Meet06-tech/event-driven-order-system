package com.meet.order_service.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private String userId;

    private String productId;

    private int quantity;

    private double price;

    private double totalAmount;

    private String status;

    private LocalDateTime createdAt;
}
