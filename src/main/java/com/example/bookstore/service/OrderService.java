package com.example.bookstore.service;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;

public interface OrderService {

    Long checkout(Long customerId);

    // =========================
    // REPORTS
    // =========================
    List<Map<String, Object>> getTop5CustomersBySpending();

    List<Map<String, Object>> getTop10SellingBooks();

    BigDecimal getTotalSalesByDate(LocalDate date);

    BigDecimal getTotalSalesPreviousMonth();

    List<Map<String, Object>> getCustomerOrderHistory(Long customerId);

    Long getPublisherOrderCountForBook(String isbn);
}