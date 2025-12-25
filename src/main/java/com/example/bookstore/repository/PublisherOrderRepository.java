package com.example.bookstore.repository;

import com.example.bookstore.entity.PublisherOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PublisherOrderRepository extends JpaRepository<PublisherOrder, Long> {

    /**
     * Retrieves all pending publisher orders with their associated book details.
     * 
     * This query joins across four tables to provide a complete picture:
     * - PublisherOrder: The main order record with status and date
     * - Publisher_Order_Book: The junction table linking orders to books with quantities
     * - Book: The book details including title and ISBN
     * - Publisher: The publisher name for display purposes
     * 
     * Only orders with status 'Pending' are returned, as confirmed orders
     * have already been processed and don't need admin action.
     * 
     * @return List of Maps where each map contains order and book information
     */
    @Query(value = """
            SELECT 
                po.publisher_order_id AS "orderId",
                po.order_date AS "orderDate",
                po.order_quantity AS "orderQuantity",
                po.status AS "status",
                pob.book_isbn AS "isbn",
                b.title AS "bookTitle",
                pob.quantity AS "quantity",
                p.name AS "publisherName"
            FROM PublisherOrder po
            JOIN Publisher_Order_Book pob ON po.publisher_order_id = pob.publisher_order_id
            JOIN Book b ON pob.book_isbn = b.isbn
            JOIN Publisher p ON b.publisher_id = p.publisher_id
            WHERE po.status = 'Pending'
            ORDER BY po.order_date DESC
            """, nativeQuery = true)
    List<Map<String, Object>> findPendingOrdersWithDetails();
}
