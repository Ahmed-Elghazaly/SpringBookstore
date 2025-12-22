package com.example.bookstore.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PublisherOrderBookId implements Serializable {

    private Long publisherOrderId;
    private String bookIsbn;

    protected PublisherOrderBookId() {
        // required by JPA
    }

    public PublisherOrderBookId(Long publisherOrderId, String bookIsbn) {
        this.publisherOrderId = publisherOrderId;
        this.bookIsbn = bookIsbn;
    }

    public Long getPublisherOrderId() {
        return publisherOrderId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublisherOrderBookId that)) return false;
        return Objects.equals(publisherOrderId, that.publisherOrderId)
                && Objects.equals(bookIsbn, that.bookIsbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publisherOrderId, bookIsbn);
    }
}
