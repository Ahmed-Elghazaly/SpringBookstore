package com.example.bookstore.service.impl;

import com.example.bookstore.entity.PublisherOrder;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.PublisherOrderRepository;
import com.example.bookstore.service.PublisherOrderService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Implementation of PublisherOrderService.
 * 
 * Publisher orders are automatically created by a database trigger when a book's
 * stock drops below its threshold quantity. This service handles confirming those
 * orders, which increases the book's stock.
 * 
 * Key Design Decision: We use a simplified schema where each publisher order
 * is for exactly one book. The old publisher_order_book junction table was
 * eliminated since our trigger creates one order per book.
 */
@Service
@Transactional
public class PublisherOrderServiceImpl implements PublisherOrderService {

    private final PublisherOrderRepository publisherOrderRepository;
    private final BookRepository bookRepository;

    public PublisherOrderServiceImpl(
            PublisherOrderRepository publisherOrderRepository,
            BookRepository bookRepository
    ) {
        this.publisherOrderRepository = publisherOrderRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * Get all pending publisher orders with their book and publisher details.
     * This data is displayed in the admin UI for order confirmation.
     * 
     * @return List of maps containing order details (orderId, orderDate, quantity, 
     *         status, isbn, bookTitle, publisherName)
     */
    @Override
    public List<Map<String, Object>> getPendingOrders() {
        return publisherOrderRepository.findPendingOrdersWithDetails();
    }

    /**
     * Confirm a publisher order.
     * 
     * When confirmed:
     * 1. The order status changes from 'Pending' to 'Confirmed'
     * 2. The book's stock quantity is increased by the order quantity
     * 
     * Business Logic: This simulates receiving a shipment from the publisher
     * and adding the books to inventory.
     * 
     * @param publisherOrderId The ID of the order to confirm
     * @throws IllegalArgumentException if order not found
     * @throws IllegalStateException if order is not in Pending status
     */
    @Override
    public void confirmOrder(Long publisherOrderId) {
        // 1. Fetch the publisher order from database
        PublisherOrder order = publisherOrderRepository.findById(publisherOrderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Publisher order not found with id: " + publisherOrderId));

        // 2. Validate and change status to Confirmed
        // The confirm() method throws IllegalStateException if not Pending
        order.confirm();

        // 3. Increase stock for the book
        // In our simplified schema, each publisher order is for exactly one book
        bookRepository.increaseStock(order.getBook().getIsbn(), order.getQuantity());

        // 4. Save the order with updated status
        publisherOrderRepository.save(order);
    }
}
