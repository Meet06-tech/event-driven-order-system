package com.meet.order_service.service;

import com.meet.order_service.config.RabbitMQConfig;
import com.meet.order_service.event.OrderCreatedEvent;
import com.meet.order_service.model.Order;
import com.meet.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository,  RabbitTemplate rabbitTemplate) {
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }



    public Order createOrder(Order order){

        order.setStatus("Created");
        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event =  new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity()
        );



        rabbitTemplate.convertAndSend(
                "order.exchange",
                "",
                event
        );
        return savedOrder;
    }
}
