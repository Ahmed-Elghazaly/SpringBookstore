package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "ShoppingCart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;

    @OneToMany(mappedBy = "cart")
    private List<CartBook> cartBooks;

    protected ShoppingCart() {
    }

    public ShoppingCart(Customer customer) {
        this.customer = customer;
    }

    public Long getCartId() {
        return cartId;
    }

    public List<CartBook> getCartBooks() {
        return cartBooks;
    }

    public Customer getCustomer() {
        return customer;
    }
}