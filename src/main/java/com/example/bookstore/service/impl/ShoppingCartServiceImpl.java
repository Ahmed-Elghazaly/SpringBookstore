package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CartItemRequest;
import com.example.bookstore.dto.CartResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartBookRepository;
import com.example.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Implementation of ShoppingCartService.
 * 
 * Key Design Decision: We use customerId directly instead of cartId because
 * the ShoppingCart table was eliminated (it was 1:1 with Customer, so we
 * merged them by having cart_book reference customer_id directly).
 */
@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final CartBookRepository cartBookRepository;
    private final BookRepository bookRepository;

    public ShoppingCartServiceImpl(CartBookRepository cartBookRepository, BookRepository bookRepository) {
        this.cartBookRepository = cartBookRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public CartResponse getCart(Long customerId) {
        // Fetch cart items using customer_id (not cart_id)
        var rows = cartBookRepository.findItemsByCustomerId(customerId);

        // Map projection results to CartItem DTOs
        List<CartResponse.CartItem> items = rows.stream().map(row -> {
            BigDecimal subtotal = row.getPrice().multiply(BigDecimal.valueOf(row.getQuantity()));
            return new CartResponse.CartItem(
                    row.getIsbn(),
                    row.getTitle(),
                    row.getPrice(),
                    row.getQuantity(),
                    subtotal
            );
        }).toList();

        // Calculate total price
        BigDecimal total = items.stream()
                .map(CartResponse.CartItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponse(items, total);
    }

    @Override
    public CartResponse addBookToCart(Long customerId, CartItemRequest request) {
        // Validate book exists
        Book book = bookRepository.findById(request.isbn())
                .orElseThrow(() -> new RuntimeException("Book not found: " + request.isbn()));

        // Check if there's enough stock
        if (book.getStockQuantity() < request.quantity()) {
            throw new RuntimeException("Not enough stock. Available: " + book.getStockQuantity());
        }

        // Add to cart using upsert (inserts if not exists, adds quantity if exists)
        cartBookRepository.upsertCartItem(customerId, request.isbn(), request.quantity());

        return getCart(customerId);
    }

    @Override
    public CartResponse updateBookQuantity(Long customerId, String isbn, int quantity) {
        if (quantity <= 0) {
            // If quantity is 0 or negative, remove the item
            return removeBookFromCart(customerId, isbn);
        }

        // Check stock availability
        Book book = bookRepository.findById(isbn)
                .orElseThrow(() -> new RuntimeException("Book not found: " + isbn));
        
        if (book.getStockQuantity() < quantity) {
            throw new RuntimeException("Not enough stock. Available: " + book.getStockQuantity());
        }

        cartBookRepository.updateQuantity(customerId, isbn, quantity);
        return getCart(customerId);
    }

    @Override
    public CartResponse removeBookFromCart(Long customerId, String isbn) {
        cartBookRepository.removeItem(customerId, isbn);
        return getCart(customerId);
    }

    @Override
    public void clearCart(Long customerId) {
        cartBookRepository.clearCart(customerId);
    }
}
