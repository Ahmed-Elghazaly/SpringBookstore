package com.example.bookstore.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CartBookId implements Serializable {

    private Long cartId;
    private String bookIsbn;

    protected CartBookId() {
        // required by JPA
    }

    public CartBookId(Long cartId, String bookIsbn) {
        this.cartId = cartId;
        this.bookIsbn = bookIsbn;
    }

    public Long getCartId() {
        return cartId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartBookId that)) return false;
        return Objects.equals(cartId, that.cartId) && Objects.equals(bookIsbn, that.bookIsbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartId, bookIsbn);
    }
}
