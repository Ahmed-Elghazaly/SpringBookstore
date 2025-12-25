package com.example.bookstore.dto;

import java.time.LocalDate;

public record CheckoutRequest(
        String creditCardNumber,
        LocalDate expiryDate
) {}