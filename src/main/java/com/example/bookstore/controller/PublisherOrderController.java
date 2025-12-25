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

    /**
     * Get all pending publisher orders with book details.
     * This endpoint is used by the admin UI to display orders that need confirmation.
     */
    @GetMapping("/pending")
    public List<Map<String, Object>> getPendingOrders() {
        return service.getPendingOrders();
    }

    /**
     * Confirm a publisher order.
     * When confirmed:
     * - Order status changes from 'Pending' to 'Confirmed'
     * - Book stock quantities are increased by the ordered amounts
     */
    @PostMapping("/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder(@PathVariable Long orderId) {
        service.confirmOrder(orderId);
    }
}
