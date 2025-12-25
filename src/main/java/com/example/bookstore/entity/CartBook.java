package com.example.bookstore.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cart_book")
public class CartBook {

    @EmbeddedId
    private CartBookId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("customerId")
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookIsbn")
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @Column(nullable = false)
    private Integer quantity;

    public CartBook() {
    }

    public CartBook(Customer customer, Book book, Integer quantity) {
        this.customer = customer;
        this.book = book;
        this.quantity = quantity;
        this.id = new CartBookId(customer.getCustomerId(), book.getIsbn());
    }

    public CartBookId getId() { return id; }
    public void setId(CartBookId id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    @Embeddable
    public static class CartBookId implements Serializable {
        
        @Column(name = "customer_id")
        private Long customerId;

        @Column(name = "book_isbn")
        private String bookIsbn;

        public CartBookId() {
        }

        public CartBookId(Long customerId, String bookIsbn) {
            this.customerId = customerId;
            this.bookIsbn = bookIsbn;
        }

        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public String getBookIsbn() { return bookIsbn; }
        public void setBookIsbn(String bookIsbn) { this.bookIsbn = bookIsbn; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartBookId that = (CartBookId) o;
            return Objects.equals(customerId, that.customerId) && Objects.equals(bookIsbn, that.bookIsbn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, bookIsbn);
        }
    }
}
