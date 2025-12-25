package com.example.bookstore.controller;

import com.example.bookstore.service.PublisherOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/publisher-orders")
public class PublisherOrderController {

    private final PublisherOrderService service;

    public PublisherOrderController(PublisherOrderService service) {
        this.service = service;
    }

    @GetMapping("/pending")
    public List<Map<String, Object>> getPendingOrders() {
        return service.getPendingOrders();
    }

    @PostMapping("/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder(@PathVariable Long orderId) {
        service.confirmOrder(orderId);
    }
}
