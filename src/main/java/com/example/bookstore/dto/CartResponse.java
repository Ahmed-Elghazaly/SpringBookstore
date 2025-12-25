package com.example.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

public record CartResponse(
        Long cartId,
        List<CartItem> items,
        BigDecimal totalPrice
) {

    public static CartResponse from(Long cartId, List<CartItem> items, BigDecimal totalPrice) {
        return new CartResponse(cartId, items, totalPrice);
    }

    public record CartItem(
            String isbn,
            String title,
            BigDecimal price,
            int quantity,
            BigDecimal subtotal
    ) {
    }
}