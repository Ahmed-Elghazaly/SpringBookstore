package com.example.bookstore.dto;

public record CartItemRequest(
        String isbn,
        int quantity
) {}
