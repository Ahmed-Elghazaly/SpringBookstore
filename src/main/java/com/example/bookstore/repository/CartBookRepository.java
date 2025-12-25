package com.example.bookstore.repository;

import com.example.bookstore.entity.CartBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Repository for cart_book table operations.
 * 
 * Key Design: All operations use customer_id directly (not cart_id) because
 * the ShoppingCart table was eliminated in the simplified schema.
 */
@Repository
public interface CartBookRepository extends JpaRepository<CartBook, CartBook.CartBookId> {

    /**
     * Projection interface for cart items with book details.
     * This allows us to fetch only the needed columns in one query.
     */
    interface CartItemProjection {
        String getIsbn();
        String getTitle();
        BigDecimal getPrice();
        Integer getQuantity();
    }

    /**
     * Find all items in a customer's cart with book details.
     * Uses a native SQL query for efficiency.
     */
    @Query(value = """
            SELECT b.isbn AS isbn, b.title AS title, b.selling_price AS price, cb.quantity AS quantity
            FROM cart_book cb
            JOIN book b ON cb.book_isbn = b.isbn
            WHERE cb.customer_id = :customerId
            ORDER BY b.title
            """, nativeQuery = true)
    List<CartItemProjection> findItemsByCustomerId(@Param("customerId") Long customerId);

    /**
     * Add or update item in cart (upsert operation).
     * If the book is already in the cart, adds to the existing quantity.
     * If not, creates a new cart entry.
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO cart_book (customer_id, book_isbn, quantity)
            VALUES (:customerId, :isbn, :quantity)
            ON CONFLICT (customer_id, book_isbn)
            DO UPDATE SET quantity = cart_book.quantity + :quantity
            """, nativeQuery = true)
    void upsertCartItem(@Param("customerId") Long customerId,
                        @Param("isbn") String isbn,
                        @Param("quantity") Integer quantity);

    /**
     * Update the quantity of a specific book in the cart.
     * This sets the quantity directly (does not add to existing).
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE cart_book SET quantity = :quantity
            WHERE customer_id = :customerId AND book_isbn = :isbn
            """, nativeQuery = true)
    void updateQuantity(@Param("customerId") Long customerId,
                        @Param("isbn") String isbn,
                        @Param("quantity") Integer quantity);

    /**
     * Remove a specific book from the cart.
     */
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM cart_book
            WHERE customer_id = :customerId AND book_isbn = :isbn
            """, nativeQuery = true)
    void removeItem(@Param("customerId") Long customerId, @Param("isbn") String isbn);

    /**
     * Clear all items from a customer's cart.
     * Called after successful checkout.
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_book WHERE customer_id = :customerId", nativeQuery = true)
    void clearCart(@Param("customerId") Long customerId);

    /**
     * Projection interface for checkout operations.
     * Returns minimal data needed for order creation.
     */
    interface CheckoutItemProjection {
        String getIsbn();
        Integer getQuantity();
    }

    /**
     * Get cart items for checkout.
     * Used by OrderServiceImpl to create order_book entries.
     */
    @Query(value = """
            SELECT cb.book_isbn AS isbn, cb.quantity AS quantity
            FROM cart_book cb
            WHERE cb.customer_id = :customerId
            """, nativeQuery = true)
    List<CheckoutItemProjection> findCheckoutItems(@Param("customerId") Long customerId);
}
