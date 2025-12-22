package com.example.bookstore.service.impl;

import com.example.bookstore.dto.CartItemRequest;
import com.example.bookstore.dto.CartResponse;
import com.example.bookstore.repository.CartBookRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.ShoppingCartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final CartBookRepository cartBookRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, CartBookRepository cartBookRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.cartBookRepository = cartBookRepository;
    }

    @Override
    public CartResponse getCart(Long customerId) {

        Long cartId = getCartIdOrThrow(customerId);

        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> new CartResponse.CartItem(row.getIsbn(), row.getQuantity())).toList();

        return CartResponse.from(cartId, items);
    }

    @Override
    public CartResponse addBookToCart(Long customerId, CartItemRequest request) {

        Long cartId = shoppingCartRepository.findCartIdByCustomerId(customerId).orElseGet(() -> shoppingCartRepository.createCart(customerId));

        cartBookRepository.upsertBook(cartId, request.isbn(), request.quantity());

        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> new CartResponse.CartItem(row.getIsbn(), row.getQuantity())).toList();

        return CartResponse.from(cartId, items);
    }

    @Override
    public CartResponse updateBookQuantity(Long customerId, String isbn, int quantity) {

        Long cartId = getCartIdOrThrow(customerId);

        cartBookRepository.updateQuantity(cartId, isbn, quantity);

        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> new CartResponse.CartItem(row.getIsbn(), row.getQuantity())).toList();

        return CartResponse.from(cartId, items);
    }

    @Override
    public CartResponse removeBookFromCart(Long customerId, String isbn) {

        Long cartId = getCartIdOrThrow(customerId);

        cartBookRepository.removeBook(cartId, isbn);

        var rows = cartBookRepository.findItemsByCartId(cartId);

        List<CartResponse.CartItem> items = rows.stream().map(row -> new CartResponse.CartItem(row.getIsbn(), row.getQuantity())).toList();

        return CartResponse.from(cartId, items);
    }

    private Long getCartIdOrThrow(Long customerId) {
        return shoppingCartRepository.findCartIdByCustomerId(customerId).orElseThrow(() -> new RuntimeException("Shopping cart does not exist for customer " + customerId));
    }
}
