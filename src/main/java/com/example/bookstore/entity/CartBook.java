package com.example.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Cart_Book")
public class CartBook {

    @EmbeddedId
    private CartBookId id;

    @ManyToOne(optional = false)
    @MapsId("cartId")
    private ShoppingCart cart;

    @ManyToOne(optional = false)
    @MapsId("bookIsbn")
    private Book book;

    @Column(nullable = false)
    private int quantity;

    protected CartBook() {
    }

    public CartBook(ShoppingCart cart, Book book, int quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
        this.id = new CartBookId(cart.getCartId(), book.getIsbn());
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }
}
