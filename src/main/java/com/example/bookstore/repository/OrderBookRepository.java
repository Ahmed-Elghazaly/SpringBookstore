package com.example.bookstore.repository;

import com.example.bookstore.entity.OrderBook;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Repository
public interface OrderBookRepository extends Repository<OrderBook, Long> {

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO order_book (order_id, book_isbn, quantity, price_at_purchase)
            VALUES (:orderId, :bookIsbn, :quantity, :priceAtPurchase)
            """, nativeQuery = true)
    void insertOrderBook(
            @Param("orderId") Long orderId,
            @Param("bookIsbn") String bookIsbn,
            @Param("quantity") Integer quantity,
            @Param("priceAtPurchase") BigDecimal priceAtPurchase
    );

    @Query(value = """
            SELECT b.isbn AS "isbn",
                   b.title AS "title",
                   SUM(ob.quantity) AS "totalSold"
            FROM order_book ob
            JOIN book b ON b.isbn = ob.book_isbn
            JOIN "order" o ON o.order_id = ob.order_id
            WHERE o.order_date >= CURRENT_DATE - INTERVAL '3 months'
            GROUP BY b.isbn, b.title
            ORDER BY "totalSold" DESC
            LIMIT 10
            """, nativeQuery = true)
    List<Map<String, Object>> findTop10SellingBooks();
}