package com.meet.order_service.model;

public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    PAYMENT_COMPLETED,
    CONFIRMED,
    INVENTORY_FAILED,
    PAYMENT_FAILED,
    CANCELLED
}
