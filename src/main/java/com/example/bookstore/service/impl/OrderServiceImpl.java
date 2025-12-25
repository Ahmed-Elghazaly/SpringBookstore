package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CheckoutRequest;
import com.example.bookstore.entity.Book;
import com.example.bookstore.entity.Customer;
import com.example.bookstore.entity.Order;
import com.example.bookstore.repository.*;
import com.example.bookstore.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Implementation of OrderService.
 * Handles checkout process and order-related reports.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final CartBookRepository cartBookRepository;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            OrderBookRepository orderBookRepository,
            BookRepository bookRepository,
            CustomerRepository customerRepository,
            CartBookRepository cartBookRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.cartBookRepository = cartBookRepository;
    }

    /**
     * Process checkout for a customer.
     * 
     * This method:
     * 1. Validates the credit card
     * 2. Retrieves cart items using customer_id (not cart_id - we eliminated ShoppingCart table)
     * 3. Validates stock availability for each item
     * 4. Creates an Order record
     * 5. Creates OrderBook records for each item
     * 6. Reduces stock quantities
     * 7. Clears the customer's cart
     * 
     * @param customerId The customer placing the order
     * @param request Credit card information
     * @return The ID of the created order
     * @throws RuntimeException if validation fails or cart is empty
     */
    @Override
    public Long checkout(Long customerId, CheckoutRequest request) {
        // 1. Validate credit card (simple validation - just check length)
        if (request.creditCardNumber() == null || request.creditCardNumber().length() < 13) {
            throw new RuntimeException("Invalid credit card number");
        }

        // 2. Get cart items directly using customer_id
        // (We use findCheckoutItems which returns isbn and quantity)
        var cartItems = cartBookRepository.findCheckoutItems(customerId);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Create the order
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order(customer, LocalDate.now());
        order = orderRepository.save(order);
        order = orderRepository.save(order);

        // 4. Process each cart item
        for (var cartItem : cartItems) {
            Book book = bookRepository.findById(cartItem.getIsbn())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + cartItem.getIsbn()));

            // Check stock availability
            if (book.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + book.getTitle() + 
                        ". Available: " + book.getStockQuantity() + 
                        ", Requested: " + cartItem.getQuantity());
            }

            // Create order_book entry (records price at time of purchase)
            orderBookRepository.insertOrderBook(
                    order.getOrderId(),
                    book.getIsbn(),
                    cartItem.getQuantity(),
                    book.getSellingPrice()
            );

            // Reduce stock (the trigger will prevent negative stock)
            bookRepository.reduceStock(book.getIsbn(), cartItem.getQuantity());
        }

        // 5. Clear the cart
        cartBookRepository.clearCart(customerId);

        return order.getOrderId();
    }

    // ==================== REPORT METHODS ====================

    /**
     * Report: Top 5 customers by total spending in the last 3 months.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTop5CustomersBySpending() {
        return orderRepository.findTop5CustomersBySpending();
    }

    /**
     * Report: Top 10 selling books by quantity in the last 3 months.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTop10SellingBooks() {
        return orderBookRepository.findTop10SellingBooks();
    }

    /**
     * Report: Total sales for a specific date.
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesByDate(LocalDate date) {
        return orderRepository.getTotalSalesByDate(date);
    }

    /**
     * Report: Total sales for the previous month.
     */
    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesPreviousMonth() {
        return orderRepository.getTotalSalesPreviousMonth();
    }

    /**
     * Get order history for a specific customer.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCustomerOrderHistory(Long customerId) {
        return orderRepository.findOrderHistoryByCustomerId(customerId);
    }


}
