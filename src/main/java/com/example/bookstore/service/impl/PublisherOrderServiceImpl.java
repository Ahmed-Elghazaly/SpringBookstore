package com.example.bookstore.service.impl;

import com.example.bookstore.entity.PublisherOrder;
import com.example.bookstore.entity.PublisherOrderBook;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.PublisherOrderRepository;
import com.example.bookstore.service.PublisherOrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        // Delegate to repository which uses a native SQL query to fetch all pending orders
        // with their associated book details in a single efficient query
        return publisherOrderRepository.findPendingOrdersWithDetails();
    }

    @Override
    public void confirmOrder(Long publisherOrderId) {
        // 1. Fetch the publisher order from database
        PublisherOrder order = publisherOrderRepository.findById(publisherOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Publisher order not found with id: " + publisherOrderId));

        // 2. Change status from Pending to Confirmed (throws if not Pending)
        order.confirm();

        // 3. Increase stock for each book in this order
        for (PublisherOrderBook item : order.getPublisherOrderBooks()) {
            bookRepository.increaseStock(
                    item.getBook().getIsbn(),
                    item.getQuantity()
            );
        }

        // 4. Persist the order status change
        publisherOrderRepository.save(order);
    }
}
