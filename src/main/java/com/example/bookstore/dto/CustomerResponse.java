package com.example.bookstore.dto;

public record CustomerResponse(Long id, String username, String email, String firstName, String lastName,
                               String shippingAddress, String phoneNumber) {
}