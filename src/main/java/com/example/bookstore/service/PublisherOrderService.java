package com.example.bookstore.service;

import java.util.List;
import java.util.Map;

public interface PublisherOrderService {

    /**
     * Gets all pending publisher orders with their book details.
     * Returns a list of maps containing order information and associated books.
     *
     * @return List of pending orders with book details
     */
    List<Map<String, Object>> getPendingOrders();

    /**
     * Confirms a publisher order.
     * Business meaning:
     * - The order status changes from PENDING → CONFIRMED
     * - Book stock quantities are increased accordingly
     *
     * @param publisherOrderId the ID of the publisher order
     * @throws IllegalArgumentException if order not found
     * @throws IllegalStateException if order is not in Pending status
     */
    void confirmOrder(Long publisherOrderId);
}
