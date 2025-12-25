package com.example.bookstore.dto;

import jakarta.validation.constraints.Email;

public record UpdateCustomerRequest(
        String password,
        @Email String email,
        String firstName,
        String lastName,
        String shippingAddress,
        String phoneNumber
) {}