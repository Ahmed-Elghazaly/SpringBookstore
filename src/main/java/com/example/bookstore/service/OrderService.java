package com.example.bookstore.service;

import com.example.bookstore.dto.CheckoutRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderService {
    Long checkout(Long customerId, CheckoutRequest request);
    List<Map<String, Object>> getTop5CustomersBySpending();
    List<Map<String, Object>> getTop10SellingBooks();
    BigDecimal getTotalSalesByDate(LocalDate date);
    BigDecimal getTotalSalesPreviousMonth();
    List<Map<String, Object>> getCustomerOrderHistory(Long customerId);
    Long getPublisherOrderCountForBook(String isbn);
}
