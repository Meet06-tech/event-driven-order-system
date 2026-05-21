package com.meet.payment_service.service;

import com.meet.event.InventoryReservedEvent;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PaymentServiceTest {

    private final PaymentService paymentService = new PaymentService();

    @Test
    void processPaymentSucceedsWhenReservedQuantityIsPositive() {
        InventoryReservedEvent event = new InventoryReservedEvent("ORDER-1", "P101", 2, 98);

        assertThat(paymentService.processPayment(event)).isTrue();
    }

    @Test
    void processPaymentFailsWhenReservedQuantityIsInvalid() {
        InventoryReservedEvent event = new InventoryReservedEvent("ORDER-1", "P101", 0, 100);

        assertThat(paymentService.processPayment(event)).isFalse();
    }
}
