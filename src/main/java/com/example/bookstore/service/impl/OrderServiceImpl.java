package com.example.bookstore.service.impl;

import com.example.bookstore.entity.Book;
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
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final CartBookRepository cartBookRepository;  // ADD THIS LINE

    public OrderServiceImpl(OrderRepository orderRepository, OrderBookRepository orderBookRepository,
                            ShoppingCartRepository shoppingCartRepository, BookRepository bookRepository,
                            CustomerRepository customerRepository, CartBookRepository cartBookRepository) {  // ADD PARAMETER
        this.orderRepository = orderRepository;
        this.orderBookRepository = orderBookRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.cartBookRepository = cartBookRepository;  // ADD THIS LINE
    }

    @Override
    public Long checkout(Long customerId) {
        Long cartId = shoppingCartRepository.findCartIdByCustomerId(customerId).orElseThrow(() -> new IllegalStateException("No cart found for customer " + customerId));

        // VALIDATE STOCK BEFORE PROCEEDING
        List<CartBookRepository.CartItemRow> cartItems = cartBookRepository.findItemsByCartId(cartId);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout with an empty cart");
        }
        for (CartBookRepository.CartItemRow item : cartItems) {
            Book book = bookRepository.findByIsbn(item.getIsbn()).orElseThrow(() -> new IllegalStateException("Book not found: " + item.getIsbn()));
            if (book.getStockQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for '" + book.getTitle() + "'. " + "Available: " + book.getStockQuantity() + ", Requested: " + item.getQuantity());
            }
        }

        Order order = new Order(LocalDate.now());
        order.setCustomer(customerRepository.getReferenceById(customerId));

        Order savedOrder = orderRepository.save(order);
        Long orderId = savedOrder.getOrderId();

        orderBookRepository.insertOrderItemsFromCart(orderId, cartId);
        bookRepository.reduceStockFromCart(cartId);
        orderBookRepository.deleteCartItems(cartId);

        return orderId;
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