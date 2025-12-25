package com.example.bookstore.controller;

import com.example.bookstore.dto.CartItemRequest;
import com.example.bookstore.dto.CartResponse;
import com.example.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{customerId}")
    public CartResponse getCart(@PathVariable Long customerId) {
        return shoppingCartService.getCart(customerId);
    }

    @PostMapping("/{customerId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse addItem(@PathVariable Long customerId, @RequestBody CartItemRequest request) {
        return shoppingCartService.addBookToCart(customerId, request);
    }

    @PutMapping("/{customerId}/items/{isbn}")
    public CartResponse updateItem(@PathVariable Long customerId, @PathVariable String isbn, @RequestParam int quantity) {
        return shoppingCartService.updateBookQuantity(customerId, isbn, quantity);
    }

    @DeleteMapping("/{customerId}/items/{isbn}")
    public CartResponse removeItem(@PathVariable Long customerId, @PathVariable String isbn) {
        return shoppingCartService.removeBookFromCart(customerId, isbn);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable Long customerId) {
        shoppingCartService.clearCart(customerId);
    }
}
