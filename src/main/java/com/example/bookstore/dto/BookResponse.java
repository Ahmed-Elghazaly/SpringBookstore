package com.example.bookstore.dto;

import java.math.BigDecimal;

public record BookResponse(
        String isbn,
        String title,
        BigDecimal sellingPrice,
        int stockQuantity
) {
}