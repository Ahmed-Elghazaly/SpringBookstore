package com.example.bookstore.repository;

import com.example.bookstore.entity.PublisherOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface PublisherOrderRepository extends JpaRepository<PublisherOrder, Long> {

    @Query(value = """
            SELECT po.publisher_order_id AS "orderId",
                   po.order_date AS "orderDate",
                   po.quantity AS "quantity",
                   po.status AS "status",
                   b.isbn AS "isbn",
                   b.title AS "bookTitle",
                   p.name AS "publisherName"
            FROM publisher_order po
            JOIN book b ON po.book_isbn = b.isbn
            JOIN publisher p ON b.publisher_id = p.publisher_id
            WHERE po.status = 'Pending'
            ORDER BY po.order_date DESC
            """, nativeQuery = true)
    List<Map<String, Object>> findPendingOrdersWithDetails();
}