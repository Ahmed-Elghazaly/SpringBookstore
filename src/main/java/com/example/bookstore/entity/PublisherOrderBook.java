package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Publisher_Order_Book")
public class PublisherOrderBook {

    @EmbeddedId
    private PublisherOrderBookId id;

    @ManyToOne(optional = false)
    @MapsId("publisherOrderId")
    private PublisherOrder publisherOrder;

    @ManyToOne(optional = false)
    @MapsId("bookIsbn")
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(
            name = "price_at_order",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal priceAtOrder;

    protected PublisherOrderBook() {
    }

    public PublisherOrderBook(
            PublisherOrder publisherOrder,
            Book book,
            int quantity,
            BigDecimal priceAtOrder
    ) {
        this.publisherOrder = publisherOrder;
        this.book = book;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
        this.id = new PublisherOrderBookId(
                publisherOrder.getPublisherOrderId(),
                book.getIsbn()
        );
    }

    public PublisherOrder getPublisherOrder() {
        return publisherOrder;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPriceAtOrder() {
        return priceAtOrder;
    }
}
