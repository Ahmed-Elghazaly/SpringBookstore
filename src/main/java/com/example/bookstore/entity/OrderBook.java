package com.example.bookstore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_book")
public class OrderBook {

    @EmbeddedId
    private OrderBookId id;

    @ManyToOne(optional = false)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false)
    @MapsId("bookIsbn")
    @JoinColumn(name = "book_isbn")
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(name = "price_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase;

    protected OrderBook() {
    }

    public OrderBook(Order order, Book book, int quantity, BigDecimal priceAtPurchase) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.id = new OrderBookId(order.getOrderId(), book.getIsbn());
    }

    public OrderBookId getId() { return id; }
    public Order getOrder() { return order; }
    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }
    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
}
