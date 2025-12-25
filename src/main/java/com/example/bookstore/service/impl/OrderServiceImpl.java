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

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderBookRepository orderBookRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final CartBookRepository cartBookRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderBookRepository orderBookRepository,
                            BookRepository bookRepository, CustomerRepository customerRepository,
                            CartBookRepository cartBookRepository) {
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.cartBookRepository = cartBookRepository;
    }

    @Override
    public Long checkout(Long customerId, CheckoutRequest request) {
        if (request.creditCardNumber() == null || request.creditCardNumber().length() < 13) {
            throw new RuntimeException("Invalid credit card number");
        }

        var cartItems = cartBookRepository.findCheckoutItems(customerId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order(customer, LocalDate.now());
        order = orderRepository.save(order);

        for (var cartItem : cartItems) {
            Book book = bookRepository.findById(cartItem.getIsbn())
                    .orElseThrow(() -> new RuntimeException("Book not found: " + cartItem.getIsbn()));

            if (book.getStockQuantity() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + book.getTitle());
            }

            orderBookRepository.insertOrderBook(order.getOrderId(), book.getIsbn(),
                    cartItem.getQuantity(), book.getSellingPrice());

            bookRepository.reduceStock(book.getIsbn(), cartItem.getQuantity());
        }

        cartBookRepository.clearCart(customerId);
        return order.getOrderId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTop5CustomersBySpending() {
        return orderRepository.findTop5CustomersBySpending();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getTop10SellingBooks() {
        return orderBookRepository.findTop10SellingBooks();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesByDate(LocalDate date) {
        return orderRepository.getTotalSalesByDate(date);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getTotalSalesPreviousMonth() {
        return orderRepository.getTotalSalesPreviousMonth();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCustomerOrderHistory(Long customerId) {
        return orderRepository.findOrderHistoryByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getPublisherOrderCountForBook(String isbn) {
        return orderRepository.countPublisherOrdersForBook(isbn);
    }
}
