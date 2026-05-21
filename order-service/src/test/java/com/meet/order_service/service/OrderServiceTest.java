package com.meet.order_service.service;

import com.meet.order_service.config.RabbitMQConfig;
import com.meet.order_service.dto.CreateOrderRequest;
import com.meet.order_service.event.OrderCreatedEvent;
import com.meet.order_service.model.Order;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrderSavesOrderAndPublishesOrderCreatedEvent() {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId("U101");
        request.setProductId("P101");
        request.setQuantity(2);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId("ORDER-1");
            return order;
        });

        Order savedOrder = orderService.createOrder(request);

        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(savedOrder.getPrice()).isEqualTo(100.0);
        assertThat(savedOrder.getTotalAmount()).isEqualTo(200.0);
        assertThat(savedOrder.getCreatedAt()).isNotNull();

        ArgumentCaptor<OrderCreatedEvent> eventCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(rabbitTemplate).convertAndSend(
                org.mockito.ArgumentMatchers.eq(RabbitMQConfig.EXCHANGE),
                org.mockito.ArgumentMatchers.eq(RabbitMQConfig.ORDER_CREATED_KEY),
                eventCaptor.capture()
        );

        OrderCreatedEvent event = eventCaptor.getValue();
        assertThat(event.getOrderId()).isEqualTo("ORDER-1");
        assertThat(event.getUserId()).isEqualTo("U101");
        assertThat(event.getProductId()).isEqualTo("P101");
        assertThat(event.getQuantity()).isEqualTo(2);
        assertThat(event.getTotalAmount()).isEqualTo(200.0);
    }
}
