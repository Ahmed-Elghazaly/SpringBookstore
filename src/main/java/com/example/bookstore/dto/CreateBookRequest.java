package com.example.bookstore.dto;

import java.math.BigDecimal;

public record CreateBookRequest(
        String isbn,
        String title,
        BigDecimal sellingPrice,
        int stockQuantity,
        int thresholdQuantity,
        Long publisherId,
        String categoryName
) {
}