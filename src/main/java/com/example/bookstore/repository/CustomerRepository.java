package com.example.bookstore.repository;

import com.example.bookstore.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Used during registration to prevent duplicate usernames
    boolean existsByUsername(String username);

    // Used during registration to prevent duplicate emails
    boolean existsByEmail(String email);


    // Used for lookup by email (e.g., profile, validation, future login)
    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByUsername(String username);
}