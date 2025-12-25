package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Book entity operations.
 * Provides search, stock management, and CRUD operations for books.
 */
public interface BookRepository extends JpaRepository<Book, String> {

    // ==================== BASIC LOOKUP ====================

    /**
     * Find a book by its ISBN (primary key).
     */
    Optional<Book> findByIsbn(String isbn);

    // ==================== SEARCH ====================

    /**
     * Dynamic search across multiple fields.
     * All parameters are optional - pass null to ignore a filter.
     * Uses PostgreSQL ILIKE for case-insensitive matching.
     */
    @Query(value = """
            SELECT DISTINCT b.*
            FROM book b
            LEFT JOIN publisher p ON b.publisher_id = p.publisher_id
            LEFT JOIN author_book ab ON b.isbn = ab.book_isbn
            LEFT JOIN author a ON ab.author_id = a.author_id
            WHERE (:isbn IS NULL OR b.isbn ILIKE CONCAT('%', :isbn, '%'))
              AND (:title IS NULL OR b.title ILIKE CONCAT('%', :title, '%'))
              AND (:category IS NULL OR b.category_name = :category)
              AND (:publisher IS NULL OR p.name ILIKE CONCAT('%', :publisher, '%'))
              AND (:author IS NULL OR a.name ILIKE CONCAT('%', :author, '%'))
            """, nativeQuery = true)
    List<Book> findBooksDynamic(@Param("isbn") String isbn, @Param("title") String title, @Param("category") String category, @Param("publisher") String publisher, @Param("author") String author);

    // ==================== STOCK MANAGEMENT ====================

    /**
     * Reduce the stock quantity of a book by a specific amount.
     * Used during checkout to decrease inventory.
     * <p>
     * Note: The database trigger 'trg_prevent_negative_stock' will prevent
     * stock from going below zero.
     *
     * @param isbn   The book's ISBN
     * @param amount The quantity to reduce (should be positive)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE book
            SET stock_quantity = stock_quantity - :amount
            WHERE isbn = :isbn
            """, nativeQuery = true)
    void reduceStock(@Param("isbn") String isbn, @Param("amount") int amount);

    /**
     * Increase the stock quantity of a book by a specific amount.
     * Used when confirming publisher orders to add inventory.
     * <p>
     * Note: The database trigger 'trg_auto_publisher_order' will automatically
     * create a publisher order if stock drops below the threshold.
     *
     * @param isbn   The book's ISBN
     * @param amount The quantity to add (should be positive)
     */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE book
            SET stock_quantity = stock_quantity + :amount
            WHERE isbn = :isbn
            """, nativeQuery = true)
    void increaseStock(@Param("isbn") String isbn, @Param("amount") int amount);
}
