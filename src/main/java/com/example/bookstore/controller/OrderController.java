package com.example.bookstore.controller;

import com.example.bookstore.dto.CheckoutRequest;
import com.example.bookstore.service.OrderService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout/{customerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> checkout(@PathVariable Long customerId, @RequestBody CheckoutRequest request) {
        Long orderId = orderService.checkout(customerId, request);
        return Map.of("orderId", orderId, "message", "Order placed successfully");
    }

    @GetMapping("/history/{customerId}")
    public List<Map<String, Object>> getOrderHistory(@PathVariable Long customerId) {
        return orderService.getCustomerOrderHistory(customerId);
    }

    @GetMapping("/reports/top-customers")
    public List<Map<String, Object>> getTop5Customers() {
        return orderService.getTop5CustomersBySpending();
    }

    @GetMapping("/reports/top-books")
    public List<Map<String, Object>> getTop10Books() {
        return orderService.getTop10SellingBooks();
    }

    @GetMapping("/reports/sales-by-date")
    public Map<String, Object> getSalesByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        BigDecimal total = orderService.getTotalSalesByDate(date);
        return Map.of("date", date, "totalSales", total != null ? total : BigDecimal.ZERO);
    }

    @GetMapping("/reports/sales-previous-month")
    public Map<String, Object> getSalesPreviousMonth() {
        BigDecimal total = orderService.getTotalSalesPreviousMonth();
        return Map.of("totalSales", total != null ? total : BigDecimal.ZERO);
    }


    @GetMapping("/reports/publisher-orders/{isbn}")
    public Long getPublisherOrderCount(@PathVariable String isbn) {
        return orderService.getPublisherOrderCountForBook(isbn);
    }
}
