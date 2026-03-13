package com.meet.order_service.service;

import com.meet.order_service.model.Order;
import com.meet.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createOrder(Order order){
        order.setStatus("Done");
        return orderRepository.save(order);
    }
}
