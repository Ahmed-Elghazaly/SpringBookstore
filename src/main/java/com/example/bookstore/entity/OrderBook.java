package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Order_Book")
public class OrderBook {

    @EmbeddedId
    private OrderBookId id;

    @ManyToOne(optional = false)
    @MapsId("orderId")
    private Order order;

    @ManyToOne(optional = false)
    @MapsId("bookIsbn")
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(
            name = "price_at_purchase",
            nullable = false,
            precision = 10,
            scale = 2
    )
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

    public Order getOrder() {
        return order;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }
}
