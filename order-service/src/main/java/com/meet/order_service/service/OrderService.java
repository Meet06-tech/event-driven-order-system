package com.meet.order_service.service;

import com.meet.order_service.config.RabbitMQConfig;
import com.meet.order_service.event.OrderCreatedEvent;
import com.meet.order_service.model.Order;
import com.meet.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

        //Fake pricin Right Now
        double price = 100.0;
        order.setPrice(price);

        double total = price * order.getQuantity();
        order.setTotalAmount(total);

        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event =  new OrderCreatedEvent(
                savedOrder.getId(),
                savedOrder.getUserId(),
                savedOrder.getProductId(),
                savedOrder.getQuantity(),
                savedOrder.getTotalAmount()
        );



        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_KEY,
                event
        );
        return savedOrder;
    }
}
