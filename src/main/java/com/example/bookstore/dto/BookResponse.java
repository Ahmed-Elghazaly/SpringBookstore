package com.example.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public record BookResponse(String isbn, String title, Integer publicationYear, BigDecimal sellingPrice,
                           int stockQuantity, int thresholdQuantity, String categoryName, String publisherName,
                           List<String> authors) {
}