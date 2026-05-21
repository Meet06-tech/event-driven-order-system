package com.meet.payment_service.service;

import com.meet.event.InventoryReservedEvent;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    public boolean processPayment(InventoryReservedEvent event) {
        return event.getReservedQuantity() > 0;
    }
}
