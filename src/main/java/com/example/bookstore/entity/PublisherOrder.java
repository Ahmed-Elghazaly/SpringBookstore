package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "PublisherOrder")
public class PublisherOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long publisherOrderId;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column(nullable = false)
    private int orderQuantity;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "publisherOrder")
    private List<PublisherOrderBook> publisherOrderBooks;

    protected PublisherOrder() {
    }

    public PublisherOrder(LocalDate orderDate, int orderQuantity, String status) {
        this.orderDate = orderDate;
        this.orderQuantity = orderQuantity;
        this.status = status;
    }

    public Long getPublisherOrderId() {
        return publisherOrderId;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void confirm() {
        if (!"Pending".equalsIgnoreCase(this.status)) {
            throw new IllegalStateException("Publisher order cannot be confirmed unless it is in Pending state");
        }
        this.status = "Confirmed";
    }

    public List<PublisherOrderBook> getPublisherOrderBooks() {
        return publisherOrderBooks;
    }
}