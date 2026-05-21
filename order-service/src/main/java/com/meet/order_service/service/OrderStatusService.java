package com.meet.order_service.service;

import com.meet.order_service.model.Order;
import com.meet.order_service.model.OrderStatus;
import com.meet.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusService {

    private final OrderRepository orderRepository;

    public OrderStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order updateStatus(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
