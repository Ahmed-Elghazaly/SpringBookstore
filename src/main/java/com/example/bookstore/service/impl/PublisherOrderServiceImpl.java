package com.example.bookstore.service.impl;

import com.example.bookstore.entity.PublisherOrder;
import com.example.bookstore.entity.PublisherOrderBook;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.PublisherOrderRepository;
import com.example.bookstore.service.PublisherOrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
    public void confirmOrder(Long publisherOrderId) {

        // Fetch publisher order
        PublisherOrder order = publisherOrderRepository.findById(publisherOrderId).orElseThrow(() -> new IllegalArgumentException("Publisher order not found with id: " + publisherOrderId));

        order.confirm();

        for (PublisherOrderBook item : order.getPublisherOrderBooks()) {
            bookRepository.increaseStock(
                    item.getBook().getIsbn(),
                    item.getQuantity()
            );
        }

        // 5. Persist order status change
        publisherOrderRepository.save(order);
    }
}