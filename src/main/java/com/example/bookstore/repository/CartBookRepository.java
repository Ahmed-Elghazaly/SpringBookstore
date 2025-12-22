package com.example.bookstore.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface CartBookRepository extends Repository<Object, Long> {

    /**
     * Insert a book into the cart.
     * If the book already exists in the cart, increase its quantity.
     * <p>
     * This is 100% MANUAL SQL.
     */
    @Modifying
    @Transactional
    @Query(value = """
                INSERT INTO Cart_Book (cart_id, book_isbn, quantity)
                VALUES (:cartId, :isbn, :quantity)
                ON CONFLICT (cart_id, book_isbn)
                DO UPDATE
                SET quantity = Cart_Book.quantity + EXCLUDED.quantity
            """, nativeQuery = true)
    void upsertBook(@Param("cartId") Long cartId, @Param("isbn") String isbn, @Param("quantity") int quantity);

    /**
     * Update quantity of a book in the cart.
     */
    @Modifying
    @Transactional
    @Query(value = """
                UPDATE Cart_Book
                SET quantity = :quantity
                WHERE cart_id = :cartId
                  AND book_isbn = :isbn
            """, nativeQuery = true)
    void updateQuantity(@Param("cartId") Long cartId, @Param("isbn") String isbn, @Param("quantity") int quantity);

    /**
     * Remove a book completely from the cart.
     */
    @Modifying
    @Transactional
    @Query(value = """
                DELETE FROM Cart_Book
                WHERE cart_id = :cartId
                  AND book_isbn = :isbn
            """, nativeQuery = true)
    void removeBook(@Param("cartId") Long cartId, @Param("isbn") String isbn);

    /**
     * Fetch all items in a cart.
     * Read-only SQL.
     */
    @Query(
            value = """
                    SELECT book_isbn AS isbn,
                           quantity
                    FROM Cart_Book
                    WHERE cart_id = :cartId
                    """,
            nativeQuery = true
    )
    java.util.List<CartItemRow> findItemsByCartId(@Param("cartId") Long cartId);

    /**
     * Projection interface for cart items.
     */
    interface CartItemRow {
        String getIsbn();

        int getQuantity();
    }
}
