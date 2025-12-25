package com.example.bookstore.dto;

public record AuthResponse(
        Long userId,
        String username,
        String name,
        String role
) {}
