package com.example.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        List<CartItem> items,
        BigDecimal totalPrice
) {
    public record CartItem(
            String isbn,
            String title,
            BigDecimal price,
            int quantity,
            BigDecimal subtotal
    ) {}
}
