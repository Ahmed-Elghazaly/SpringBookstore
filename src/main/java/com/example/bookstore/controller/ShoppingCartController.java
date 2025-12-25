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

    /**
     * Get the current shopping cart for a customer.
     */
    @GetMapping("/{customerId}")
    public CartResponse getCart(@PathVariable Long customerId) {
        return shoppingCartService.getCart(customerId);
    }

    /**
     * Add a book to the cart or increase its quantity.
     */
    @PostMapping("/{customerId}/items")
    public CartResponse addBookToCart(
            @PathVariable Long customerId,
            @RequestBody CartItemRequest request) {
        return shoppingCartService.addBookToCart(customerId, request);
    }

    /**
     * Update quantity of a book in the cart.
     */
    @PutMapping("/{customerId}/items/{isbn}")
    public CartResponse updateBookQuantity(
            @PathVariable Long customerId,
            @PathVariable String isbn,
            @RequestParam int quantity) {
        return shoppingCartService.updateBookQuantity(customerId, isbn, quantity);
    }

    /**
     * Remove a book from the cart.
     */
    @DeleteMapping("/{customerId}/items/{isbn}")
    public CartResponse removeBookFromCart(
            @PathVariable Long customerId,
            @PathVariable String isbn) {
        return shoppingCartService.removeBookFromCart(customerId, isbn);
    }

    @DeleteMapping("/{customerId}/clear")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable Long customerId) {
        shoppingCartService.clearCart(customerId);
    }
}
