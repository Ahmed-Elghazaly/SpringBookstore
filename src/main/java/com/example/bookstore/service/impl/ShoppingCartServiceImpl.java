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
        var rows = cartBookRepository.findItemsByCustomerId(customerId);
        List<CartResponse.CartItem> items = rows.stream().map(row -> {
            BigDecimal subtotal = row.getPrice().multiply(BigDecimal.valueOf(row.getQuantity()));
            return new CartResponse.CartItem(row.getIsbn(), row.getTitle(), row.getPrice(), row.getQuantity(), subtotal);
        }).toList();
        BigDecimal total = items.stream().map(CartResponse.CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(items, total);
    }

    @Override
    public CartResponse addBookToCart(Long customerId, CartItemRequest request) {
        Book book = bookRepository.findById(request.isbn())
                .orElseThrow(() -> new RuntimeException("Book not found: " + request.isbn()));
        cartBookRepository.upsertCartItem(customerId, request.isbn(), request.quantity());
        return getCart(customerId);
    }

    @Override
    public CartResponse updateBookQuantity(Long customerId, String isbn, int quantity) {
        if (quantity <= 0) return removeBookFromCart(customerId, isbn);
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
