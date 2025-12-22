package com.example.bookstore.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@org.springframework.stereotype.Repository
public interface OrderBookRepository extends Repository<com.example.bookstore.entity.OrderBook, Long> {

    /**
     * Insert all cart items into Order_Book for the given order.
     * Copying the quantity and the price at purchase from Book.
     */
    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO Order_Book (order_id, book_isbn, quantity, price_at_purchase)
            SELECT
                :orderId,
                cb.book_isbn,
                cb.quantity,
                b.selling_price
            FROM Cart_Book cb
            INNER JOIN Book b ON b.isbn = cb.book_isbn
            WHERE cb.cart_id = :cartId
            """, nativeQuery = true)
    void insertOrderItemsFromCart(@Param("orderId") Long orderId, @Param("cartId") Long cartId);

    /**
     * Remove all items from the cart after order creation.
     */
    @Modifying
    @Transactional
    @Query(value = """
            DELETE FROM Cart_Book
            WHERE cart_id = :cartId
            """, nativeQuery = true)
    void deleteCartItems(@Param("cartId") Long cartId);

    /**
     * Report:
     * Top 10 selling books by total quantity sold.
     */
    @Query(value = """
            SELECT
                b.isbn            AS isbn,
                b.title           AS title,
                SUM(ob.quantity)  AS total_sold
            FROM Order_Book ob
            JOIN Book b ON b.isbn = ob.book_isbn
            GROUP BY b.isbn, b.title
            ORDER BY total_sold DESC
            LIMIT 10
            """, nativeQuery = true)
    java.util.List<java.util.Map<String, Object>> findTop10SellingBooks();
}