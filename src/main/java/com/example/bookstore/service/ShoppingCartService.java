package com.example.bookstore.service;

import com.example.bookstore.dto.CartItemRequest;
import com.example.bookstore.dto.CartResponse;

public interface ShoppingCartService {

    CartResponse getCart(Long customerId);

    CartResponse addBookToCart(Long customerId, CartItemRequest request);

    CartResponse updateBookQuantity(Long customerId, String isbn, int quantity);

    CartResponse removeBookFromCart(Long customerId, String isbn);
}
