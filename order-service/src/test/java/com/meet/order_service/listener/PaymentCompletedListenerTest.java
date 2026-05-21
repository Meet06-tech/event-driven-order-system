package com.meet.order_service.listener;

import com.meet.event.PaymentCompletedEvent;
import com.meet.event.PaymentFailedEvent;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.service.OrderStatusService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class PaymentCompletedListenerTest {

    @Mock
    private OrderStatusService orderStatusService;

    @InjectMocks
    private PaymentCompletedListener listener;

    @Test
    void handlePaymentCompletedMarksOrderAsPaid() {
        PaymentCompletedEvent event = new PaymentCompletedEvent("ORDER-1");

        listener.handlePaymentCompleted(event);

        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.PAYMENT_COMPLETED);
        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.CONFIRMED);
        verifyNoMoreInteractions(orderStatusService);
    }

    @Test
    void handlePaymentFailedMarksOrderAsPaymentFailedAndCancelled() {
        PaymentFailedEvent event = new PaymentFailedEvent("ORDER-1", "Card declined");

        listener.handlePaymentFailed(event);

        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.PAYMENT_FAILED);
        verify(orderStatusService).updateStatus("ORDER-1", OrderStatus.CANCELLED);
        verifyNoMoreInteractions(orderStatusService);
    }
}
