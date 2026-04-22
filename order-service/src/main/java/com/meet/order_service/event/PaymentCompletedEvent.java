package com.meet.order_service.event;

import lombok.Data;

@Data
public class PaymentCompletedEvent {

    private String orderId;
}