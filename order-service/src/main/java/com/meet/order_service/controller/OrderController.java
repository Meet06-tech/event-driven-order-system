package com.meet.order_service.controller;

import com.meet.order_service.dto.CreateOrderRequest;
import com.meet.order_service.dto.OrderResponse;
import com.meet.order_service.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request) {
        return OrderResponse.from(orderService.createOrder(request));
    }
}
