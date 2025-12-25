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

    // Constructors
    public PublisherOrder() {}

    public PublisherOrder(Book book, Integer quantity) {
        this.book = book;
        this.quantity = quantity;
        this.orderDate = LocalDate.now();
        this.status = "Pending";
    }

    // Business method
    public void confirm() {
        if (!"Pending".equals(this.status)) {
            throw new IllegalStateException("Only pending orders can be confirmed");
        }
        this.status = "Confirmed";
    }

    // Getters and Setters
    public Long getPublisherOrderId() { return publisherOrderId; }
    public void setPublisherOrderId(Long publisherOrderId) { this.publisherOrderId = publisherOrderId; }
    public Book getBook() { return book; }
    public void setBook(Book book) { this.book = book; }
    public LocalDate getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}