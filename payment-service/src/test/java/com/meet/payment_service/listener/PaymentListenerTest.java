package com.meet.payment_service.listener;

import com.meet.event.InventoryReservedEvent;
import com.meet.event.PaymentCompletedEvent;
import com.meet.event.PaymentFailedEvent;
import com.meet.payment_service.config.RabbitMQConfig;
import com.meet.payment_service.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentListenerTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentListener listener;

    @Test
    void handlePaymentPublishesPaymentCompletedAfterInventoryReserved() {
        InventoryReservedEvent event = new InventoryReservedEvent("ORDER-1", "P101", 2, 98);
        when(paymentService.processPayment(event)).thenReturn(true);

        listener.handlePayment(event);

        ArgumentCaptor<PaymentCompletedEvent> eventCaptor = ArgumentCaptor.forClass(PaymentCompletedEvent.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.PAYMENT_COMPLETED_KEY),
                eventCaptor.capture()
        );

        assertThat(eventCaptor.getValue().getOrderId()).isEqualTo("ORDER-1");
    }

    @Test
    void handlePaymentPublishesPaymentFailedWhenProcessingFails() {
        InventoryReservedEvent event = new InventoryReservedEvent("ORDER-1", "P101", 0, 100);
        when(paymentService.processPayment(event)).thenReturn(false);

        listener.handlePayment(event);

        ArgumentCaptor<PaymentFailedEvent> eventCaptor = ArgumentCaptor.forClass(PaymentFailedEvent.class);
        verify(rabbitTemplate).convertAndSend(
                eq(RabbitMQConfig.EXCHANGE),
                eq(RabbitMQConfig.PAYMENT_FAILED_KEY),
                eventCaptor.capture()
        );

        assertThat(eventCaptor.getValue().getOrderId()).isEqualTo("ORDER-1");
        assertThat(eventCaptor.getValue().getReason()).isEqualTo("Payment validation failed");
    }
}
