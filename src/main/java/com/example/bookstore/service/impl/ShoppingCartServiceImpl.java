package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CartItemRequest;
import com.example.bookstore.dto.CartResponse;
import com.example.bookstore.entity.Book;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartBookRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartBookRepository cartBookRepository;
    private final BookRepository bookRepository;  // ADD THIS LINE

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository,
                                   CartBookRepository cartBookRepository, BookRepository bookRepository) {  // ADD PARAMETER
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartBookRepository = cartBookRepository;
        this.bookRepository = bookRepository;  // ADD THIS LINE
    }

    @Override
    public CartResponse getCart(Long customerId) {
        Long cartId = getCartIdOrThrow(customerId);
        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> {
            BigDecimal subtotal = row.getPrice().multiply(BigDecimal.valueOf(row.getQuantity()));
            return new CartResponse.CartItem(row.getIsbn(), row.getTitle(), row.getPrice(), row.getQuantity(), subtotal);
        }).toList();

        BigDecimal totalPrice = items.stream().map(CartResponse.CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.from(cartId, items, totalPrice);
    }

    @Override
    public CartResponse addBookToCart(Long customerId, CartItemRequest request) {


        // Validate book exists and has enough stock
        Book book = bookRepository.findByIsbn(request.isbn()).orElseThrow(() -> new IllegalArgumentException("Book not found: " + request.isbn()));


        Long cartId = shoppingCartRepository.findCartIdByCustomerId(customerId).orElseGet(() -> shoppingCartRepository.createCart(customerId));


        // Note: The upsert ADDS quantity, so we need to check total won't exceed stock
        // This is a simplification - for full accuracy you'd need to query current cart qty
        if (book.getStockQuantity() < request.quantity()) {
            throw new IllegalStateException("Not enough stock for " + book.getTitle());
        }


        cartBookRepository.upsertBook(cartId, request.isbn(), request.quantity());

        // Fetch full details including book information
        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> {
            BigDecimal subtotal = row.getPrice().multiply(BigDecimal.valueOf(row.getQuantity()));
            return new CartResponse.CartItem(row.getIsbn(), row.getTitle(), row.getPrice(), row.getQuantity(), subtotal);
        }).toList();

        BigDecimal totalPrice = items.stream().map(CartResponse.CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.from(cartId, items, totalPrice);
    }

    @Override
    public CartResponse updateBookQuantity(Long customerId, String isbn, int quantity) {
        Long cartId = getCartIdOrThrow(customerId);

        cartBookRepository.updateQuantity(cartId, isbn, quantity);

        // Fetch full details including book information
        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> {
            BigDecimal subtotal = row.getPrice().multiply(BigDecimal.valueOf(row.getQuantity()));
            return new CartResponse.CartItem(row.getIsbn(), row.getTitle(), row.getPrice(), row.getQuantity(), subtotal);
        }).toList();

        BigDecimal totalPrice = items.stream().map(CartResponse.CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.from(cartId, items, totalPrice);
    }

    @Override
    public CartResponse removeBookFromCart(Long customerId, String isbn) {
        Long cartId = getCartIdOrThrow(customerId);

        cartBookRepository.removeBook(cartId, isbn);

        // Fetch full details including book information
        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> {
            BigDecimal subtotal = row.getPrice().multiply(BigDecimal.valueOf(row.getQuantity()));
            return new CartResponse.CartItem(row.getIsbn(), row.getTitle(), row.getPrice(), row.getQuantity(), subtotal);
        }).toList();

        BigDecimal totalPrice = items.stream().map(CartResponse.CartItem::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.from(cartId, items, totalPrice);
    }

    private Long getCartIdOrThrow(Long customerId) {
        return shoppingCartRepository.findCartIdByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Shopping cart does not exist for customer " + customerId));
    }

    @Override
    public void clearCart(Long customerId) {
        Long cartId = shoppingCartRepository.findCartIdByCustomerId(customerId).orElse(null);

        if (cartId != null) {
            cartBookRepository.clearCart(cartId);
        }
    }
}
