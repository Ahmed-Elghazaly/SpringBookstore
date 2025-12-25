package com.example.bookstore.service;

import java.util.List;
import java.util.Map;

public interface PublisherOrderService {
    List<Map<String, Object>> getPendingOrders();
    void confirmOrder(Long publisherOrderId);
}
