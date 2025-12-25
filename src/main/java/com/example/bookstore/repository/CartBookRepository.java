package com.example.bookstore.repository;

import com.example.bookstore.entity.CartBook;
import com.example.bookstore.entity.CartBookId;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Repository
public interface CartBookRepository extends Repository<CartBook, CartBookId> {

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

    @Query(value = """
            SELECT 
                b.isbn AS isbn,
                b.title AS title,
                b.selling_price AS price,
                cb.quantity AS quantity
            FROM Cart_Book cb
            JOIN Book b ON cb.book_isbn = b.isbn
            WHERE cb.cart_id = :cartId
            """, nativeQuery = true)
    List<CartItemRow> findItemsByCartId(@Param("cartId") Long cartId);

    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM Cart_Book
            WHERE cart_id = :cartId
            """, nativeQuery = true)
    void clearCart(@Param("cartId") Long cartId);

    interface CartItemRow {
        String getIsbn();

        String getTitle();

        BigDecimal getPrice();

        int getQuantity();
    }
}