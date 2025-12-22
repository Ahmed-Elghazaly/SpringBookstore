package com.example.bookstore.dto;

import java.util.List;

public record CartResponse(
        Long cartId,
        List<CartItem> items
) {

    public static CartResponse from(Long cartId, List<CartItem> items) {
        return new CartResponse(cartId, items);
    }

    public record CartItem(
            String isbn,
            int quantity
    ) {
    }
}