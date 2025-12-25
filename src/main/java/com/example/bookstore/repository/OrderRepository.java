package com.example.bookstore.repository;

import com.example.bookstore.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query(value = """
            SELECT c.customer_id AS id, c.first_name AS firstName, c.last_name AS lastName,
                   c.email AS email, SUM(ob.quantity * ob.price_at_purchase) AS totalSpent
            FROM customer c
            JOIN "order" o ON c.customer_id = o.customer_id
            JOIN order_book ob ON o.order_id = ob.order_id
            WHERE o.order_date >= CURRENT_DATE - INTERVAL '3 months'
            GROUP BY c.customer_id, c.first_name, c.last_name, c.email
            ORDER BY totalSpent DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Map<String, Object>> findTop5CustomersBySpending();

    @Query(value = """
            SELECT COALESCE(SUM(ob.quantity * ob.price_at_purchase), 0)
            FROM "order" o
            JOIN order_book ob ON o.order_id = ob.order_id
            WHERE o.order_date = :date
            """, nativeQuery = true)
    BigDecimal getTotalSalesByDate(@Param("date") LocalDate date);

    @Query(value = """
            SELECT COALESCE(SUM(ob.quantity * ob.price_at_purchase), 0)
            FROM "order" o
            JOIN order_book ob ON o.order_id = ob.order_id
            WHERE o.order_date >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
              AND o.order_date < DATE_TRUNC('month', CURRENT_DATE)
            """, nativeQuery = true)
    BigDecimal getTotalSalesPreviousMonth();

    @Query(value = """
            SELECT o.order_id AS orderId, o.order_date AS orderDate,
                   b.isbn AS isbn, b.title AS title, ob.quantity AS quantity,
                   ob.price_at_purchase AS priceAtPurchase
            FROM "order" o
            JOIN order_book ob ON o.order_id = ob.order_id
            JOIN book b ON ob.book_isbn = b.isbn
            WHERE o.customer_id = :customerId
            ORDER BY o.order_date DESC, o.order_id
            """, nativeQuery = true)
    List<Map<String, Object>> findOrderHistoryByCustomerId(@Param("customerId") Long customerId);

    @Query(value = "SELECT COUNT(*) FROM publisher_order WHERE book_isbn = :isbn", nativeQuery = true)
    Long countPublisherOrdersForBook(@Param("isbn") String isbn);
}
