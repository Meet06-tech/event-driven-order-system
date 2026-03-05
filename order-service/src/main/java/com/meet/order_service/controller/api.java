package com.meet.order_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class api {

    @GetMapping
    public String print(){
        return "Hello Meet ";
    }
}
