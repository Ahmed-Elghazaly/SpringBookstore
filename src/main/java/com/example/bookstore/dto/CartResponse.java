package com.example.bookstore.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response DTO for shopping cart data.
 * Note: We removed cartId since cart is now directly linked to customerId
 * (the ShoppingCart table was eliminated in the simplified schema).
 */
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
    ) {
    }
}
