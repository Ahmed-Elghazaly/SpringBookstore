package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Entity representing an item in a customer's shopping cart.
 * 
 * Key Design Decision: This entity uses customer_id directly (not cart_id)
 * because we eliminated the ShoppingCart table. The relationship is now:
 * Customer (1) --- (N) CartBook (N) --- (1) Book
 * 
 * The composite primary key is (customer_id, book_isbn).
 */
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

    // ==================== CONSTRUCTORS ====================
    
    public CartBook() {
        // Required by JPA
    }

    public CartBook(Customer customer, Book book, Integer quantity) {
        this.customer = customer;
        this.book = book;
        this.quantity = quantity;
        this.id = new CartBookId(customer.getCustomerId(), book.getIsbn());
    }

    // ==================== GETTERS AND SETTERS ====================
    
    public CartBookId getId() { 
        return id; 
    }
    
    public void setId(CartBookId id) { 
        this.id = id; 
    }
    
    public Customer getCustomer() { 
        return customer; 
    }
    
    public void setCustomer(Customer customer) { 
        this.customer = customer; 
    }
    
    public Book getBook() { 
        return book; 
    }
    
    public void setBook(Book book) { 
        this.book = book; 
    }
    
    public Integer getQuantity() { 
        return quantity; 
    }
    
    public void setQuantity(Integer quantity) { 
        this.quantity = quantity; 
    }

    // ==================== COMPOSITE PRIMARY KEY ====================
    
    /**
     * Composite primary key for CartBook.
     * Consists of customer_id and book_isbn.
     * 
     * This is defined as a static inner class for convenience,
     * keeping the key definition close to the entity it belongs to.
     */
    @Embeddable
    public static class CartBookId implements Serializable {
        
        @Column(name = "customer_id")
        private Long customerId;

        @Column(name = "book_isbn")
        private String bookIsbn;

        public CartBookId() {
            // Required by JPA
        }

        public CartBookId(Long customerId, String bookIsbn) {
            this.customerId = customerId;
            this.bookIsbn = bookIsbn;
        }

        // Getters and setters
        public Long getCustomerId() { 
            return customerId; 
        }
        
        public void setCustomerId(Long customerId) { 
            this.customerId = customerId; 
        }
        
        public String getBookIsbn() { 
            return bookIsbn; 
        }
        
        public void setBookIsbn(String bookIsbn) { 
            this.bookIsbn = bookIsbn; 
        }

        // equals() and hashCode() are REQUIRED for composite keys
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CartBookId that = (CartBookId) o;
            return Objects.equals(customerId, that.customerId) 
                    && Objects.equals(bookIsbn, that.bookIsbn);
        }

        @Override
        public int hashCode() {
            return Objects.hash(customerId, bookIsbn);
        }
    }
}
