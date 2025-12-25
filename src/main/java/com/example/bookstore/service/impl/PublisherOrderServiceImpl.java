package com.example.bookstore.service.impl;

import com.example.bookstore.entity.PublisherOrder;
import com.example.bookstore.repository.PublisherOrderRepository;
import com.example.bookstore.service.PublisherOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PublisherOrderServiceImpl implements PublisherOrderService {

    private final PublisherOrderRepository publisherOrderRepository;


    public PublisherOrderServiceImpl(PublisherOrderRepository publisherOrderRepository) {
        this.publisherOrderRepository = publisherOrderRepository;
    }

    @Override
    public List<Map<String, Object>> getPendingOrders() {
        return publisherOrderRepository.findPendingOrdersWithDetails();
    }

    @Override
    public void confirmOrder(Long publisherOrderId) {
        PublisherOrder order = publisherOrderRepository.findById(publisherOrderId).orElseThrow(() -> new IllegalArgumentException("Publisher order not found: " + publisherOrderId));

        // 1. Change status in memory
        order.confirm();


        // 2. Save the order. This fires the DB trigger, which updates the stock.
        publisherOrderRepository.save(order);
    }
}