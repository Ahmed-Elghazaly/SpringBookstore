package com.example.bookstore.dto;

import java.math.BigDecimal;

public record UpdateBookRequest(
        String title,
        BigDecimal sellingPrice,
        Integer stockQuantity,
        Integer thresholdQuantity
) {}
