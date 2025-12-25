package com.example.bookstore.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "\"order\"")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "order_date", nullable = false)
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order")
    private List<OrderBook> orderBooks;

    protected Order() {
    }

    public Order(Customer customer, LocalDate orderDate) {
        this.customer = customer;
        this.orderDate = orderDate;
    }

    // Getters
    public Long getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public LocalDate getOrderDate() { return orderDate; }
    public List<OrderBook> getOrderBooks() { return orderBooks; }

    // Setters
    public void setCustomer(Customer customer) { this.customer = customer; }
    public void setOrderDate(LocalDate orderDate) { this.orderDate = orderDate; }
}
