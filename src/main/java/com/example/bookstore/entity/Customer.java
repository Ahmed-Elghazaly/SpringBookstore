package com.example.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String shippingAddress;

    protected Customer() {
    }

    public Customer(String username, String password, String email, String shippingAddress) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.shippingAddress = shippingAddress;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }
}