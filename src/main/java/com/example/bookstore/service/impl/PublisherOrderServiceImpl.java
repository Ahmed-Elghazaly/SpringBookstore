package com.example.bookstore.service.impl;

import com.example.bookstore.entity.PublisherOrder;
import com.example.bookstore.repository.BookRepository;
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
    private final BookRepository bookRepository;

    public PublisherOrderServiceImpl(PublisherOrderRepository publisherOrderRepository, BookRepository bookRepository) {
        this.publisherOrderRepository = publisherOrderRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Map<String, Object>> getPendingOrders() {
        return publisherOrderRepository.findPendingOrdersWithDetails();
    }

    @Override
    public void confirmOrder(Long publisherOrderId) {
        PublisherOrder order = publisherOrderRepository.findById(publisherOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Publisher order not found: " + publisherOrderId));
        order.confirm();
        bookRepository.increaseStock(order.getBook().getIsbn(), order.getQuantity());
        publisherOrderRepository.save(order);
    }
}
