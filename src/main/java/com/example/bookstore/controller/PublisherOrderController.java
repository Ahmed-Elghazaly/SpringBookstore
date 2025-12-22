package com.example.bookstore.controller;

import com.example.bookstore.service.PublisherOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publisher-orders")
public class PublisherOrderController {

    private final PublisherOrderService service;

    public PublisherOrderController(PublisherOrderService service) {
        this.service = service;
    }

    @PostMapping("/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder(@PathVariable Long orderId) {
        service.confirmOrder(orderId);
    }
}