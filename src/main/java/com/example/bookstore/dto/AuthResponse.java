package com.example.bookstore.dto;

public record AuthResponse(
        Long id,
        String username,
        String role, // "ADMIN" or "CUSTOMER"
        String name // First name or Admin name
) {}