package com.example.bookstore.dto;

public record LoginRequest(
        String username,
        String password,
        String role // "ADMIN" or "CUSTOMER"
) {}