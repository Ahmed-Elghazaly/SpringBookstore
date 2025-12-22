package com.example.bookstore.service;

public interface PublisherOrderService {

    /**
     * Confirms a publisher order.
     * Business meaning:
     * - The order status changes from PENDING → CONFIRMED
     * - Book stock quantities are increased accordingly
     *
     * @param publisherOrderId the ID of the publisher order
     */
    void confirmOrder(Long publisherOrderId);
}