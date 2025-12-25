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

@Repository
public interface CartBookRepository extends JpaRepository<CartBook, CartBook.CartBookId> {

    interface CartItemProjection {
        String getIsbn();
        String getTitle();
        BigDecimal getPrice();
        Integer getQuantity();
    }

    @Query(value = """
            SELECT b.isbn AS isbn, b.title AS title, b.selling_price AS price, cb.quantity AS quantity
            FROM cart_book cb
            JOIN book b ON cb.book_isbn = b.isbn
            WHERE cb.customer_id = :customerId
            ORDER BY b.title
            """, nativeQuery = true)
    List<CartItemProjection> findItemsByCustomerId(@Param("customerId") Long customerId);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO cart_book (customer_id, book_isbn, quantity)
            VALUES (:customerId, :isbn, :quantity)
            ON CONFLICT (customer_id, book_isbn)
            DO UPDATE SET quantity = cart_book.quantity + :quantity
            """, nativeQuery = true)
    void upsertCartItem(@Param("customerId") Long customerId, @Param("isbn") String isbn, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query(value = "UPDATE cart_book SET quantity = :quantity WHERE customer_id = :customerId AND book_isbn = :isbn", nativeQuery = true)
    void updateQuantity(@Param("customerId") Long customerId, @Param("isbn") String isbn, @Param("quantity") Integer quantity);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_book WHERE customer_id = :customerId AND book_isbn = :isbn", nativeQuery = true)
    void removeItem(@Param("customerId") Long customerId, @Param("isbn") String isbn);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM cart_book WHERE customer_id = :customerId", nativeQuery = true)
    void clearCart(@Param("customerId") Long customerId);

    interface CheckoutItemProjection {
        String getIsbn();
        Integer getQuantity();
    }

    @Query(value = "SELECT cb.book_isbn AS isbn, cb.quantity AS quantity FROM cart_book cb WHERE cb.customer_id = :customerId", nativeQuery = true)
    List<CheckoutItemProjection> findCheckoutItems(@Param("customerId") Long customerId);
}
