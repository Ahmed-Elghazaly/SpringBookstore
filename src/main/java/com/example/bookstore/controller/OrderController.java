package com.example.bookstore.controller;

import com.example.bookstore.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Checkout endpoint.
     * Converts the customer's shopping cart into an order.
     */
    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<Map<String, Long>> checkout(@PathVariable Long customerId) {
        Long orderId = orderService.checkout(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("orderId", orderId));
    }

    /**
     * Customer: View all past orders with details.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Map<String, Object>>> getCustomerOrderHistory(@PathVariable Long customerId) {

        return ResponseEntity.ok(orderService.getCustomerOrderHistory(customerId));
    }

    /**
     * Report: Top 5 customers by total spending.
     */
    @GetMapping("/reports/top-customers")
    public ResponseEntity<List<Map<String, Object>>> getTopCustomers() {
        return ResponseEntity.ok(orderService.getTop5CustomersBySpending());
    }

    /**
     * Report: Top 10 selling books.
     */
    @GetMapping("/reports/top-books")
    public ResponseEntity<List<Map<String, Object>>> getTopSellingBooks() {
        return ResponseEntity.ok(orderService.getTop10SellingBooks());
    }


    @GetMapping("/reports/sales/date")
    public ResponseEntity<BigDecimal> getTotalSalesByDate(@RequestParam java.time.LocalDate date) {
        return ResponseEntity.ok(orderService.getTotalSalesByDate(date));
    }

    @GetMapping("/reports/sales/prev-month")
    public ResponseEntity<BigDecimal> getTotalSalesPreviousMonth() {
        return ResponseEntity.ok(orderService.getTotalSalesPreviousMonth());
    }
}
