package com.example.bookstore.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrderBookId implements Serializable {

    private Long orderId;
    private String bookIsbn;

    protected OrderBookId() {
        // required by JPA
    }

    public OrderBookId(Long orderId, String bookIsbn) {
        this.orderId = orderId;
        this.bookIsbn = bookIsbn;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderBookId that)) return false;
        return Objects.equals(orderId, that.orderId)
                && Objects.equals(bookIsbn, that.bookIsbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, bookIsbn);
    }
}
