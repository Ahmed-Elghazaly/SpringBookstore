package com.example.bookstore.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface OrderRepository extends CrudRepository<com.example.bookstore.entity.Order, Long> {

    /* =========================
       REPORT 1: Top 5 Customers
       ========================= */
    @Query(value = """
            SELECT
                c.customer_id AS "customerId",
                c.first_name  AS "firstName",
                c.last_name   AS "lastName",
                SUM(ob.quantity * ob.price_at_purchase) AS "totalSpent"
            FROM customer c
            JOIN "Order" o ON c.customer_id = o.customer_id
            JOIN order_book ob ON o.order_id = ob.order_id
            GROUP BY c.customer_id, c.first_name, c.last_name
            ORDER BY "totalSpent" DESC
            LIMIT 5
            """, nativeQuery = true)
    List<Map<String, Object>> findTop5CustomersBySpending();

    /* =========================
       REPORT 2: Sales on Specific Day (PDF Req)
       ========================= */
    @Query(value = """
            SELECT COALESCE(SUM(ob.quantity * ob.price_at_purchase), 0)
            FROM "Order" o
            JOIN order_book ob ON o.order_id = ob.order_id
            WHERE o.order_date = :date
            """, nativeQuery = true)
    BigDecimal getTotalSalesByDate(@Param("date") LocalDate date);

    /* =========================
       REPORT 3: Sales Previous Month (PDF Req)
       ========================= */
    @Query(value = """
            SELECT COALESCE(SUM(ob.quantity * ob.price_at_purchase), 0)
            FROM "Order" o
            JOIN order_book ob ON o.order_id = ob.order_id
            WHERE o.order_date >= DATE_TRUNC('month', CURRENT_DATE - INTERVAL '1 month')
              AND o.order_date < DATE_TRUNC('month', CURRENT_DATE)
            """, nativeQuery = true)
    BigDecimal getTotalSalesPreviousMonth();

    /* =========================
       CUSTOMER HISTORY (PDF Req)
       ========================= */
    @Query(value = """
            SELECT
                o.order_id        AS "orderId",
                o.order_date      AS "orderDate",
                b.isbn            AS "isbn",
                b.title           AS "title",
                ob.quantity       AS "quantity",
                ob.price_at_purchase AS "priceAtPurchase"
            FROM "Order" o
            JOIN order_book ob ON o.order_id = ob.order_id
            JOIN book b ON ob.book_isbn = b.isbn
            WHERE o.customer_id = :customerId
            ORDER BY o.order_date DESC, o.order_id DESC
            """, nativeQuery = true)
    List<Map<String, Object>> findOrderHistoryByCustomerId(@Param("customerId") Long customerId);
}