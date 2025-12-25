package com.example.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreateBookRequest(
        String isbn,
        String title,
        Integer publicationYear,
        BigDecimal sellingPrice,
        int stockQuantity,
        int thresholdQuantity,
        Long publisherId,
        String categoryName,
        List<String> authorNames
) {}
