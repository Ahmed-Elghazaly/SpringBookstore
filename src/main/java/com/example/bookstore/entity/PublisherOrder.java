package com.example.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "publisher_order")
public class PublisherOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "publisher_order_id")
    private Long publisherOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_isbn", nullable = false)
    private Book book;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, length = 20)
    private String status;

    public PublisherOrder() {
    }

    public PublisherOrder(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
        this.orderDate = LocalDate.now();
        this.status = "Pending";
    }

    public void confirm() {
        if (!"Pending".equals(this.status)) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        this.status = "Confirmed";
    }

    public Long getPublisherOrderId() { return publisherOrderId; }
    public Book getBook() { return book; }
    public LocalDate getOrderDate() { return orderDate; }
    public Integer getQuantity() { return quantity; }
    public String getStatus() { return status; }
    
    public void setPublisherOrderId(Long id) { this.publisherOrderId = id; }
    public void setBook(Book book) { this.book = book; }
    public void setOrderDate(LocalDate date) { this.orderDate = date; }
    public void setQuantity(Integer qty) { this.quantity = qty; }
    public void setStatus(String status) { this.status = status; }
}
