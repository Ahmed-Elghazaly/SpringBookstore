package com.example.bookstore.repository;

import com.example.bookstore.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, String> {

    /* =========================
       Basic lookup
       ========================= */
    Optional<Book> findByIsbn(String isbn);

    /* =========================
       GOLDEN SEARCH: TITLE + CATEGORY + AUTHOR + PUBLISHER
       Single dynamic PostgreSQL-native query
       ========================= */
    @Query(value = """
            SELECT DISTINCT b.*
            FROM book b
            LEFT JOIN publisher p ON b.publisher_id = p.publisher_id
            LEFT JOIN author_book ab ON b.isbn = ab.book_isbn
            LEFT JOIN author a ON ab.author_id = a.author_id
            WHERE (:title IS NULL OR b.title ILIKE CONCAT('%', :title, '%'))
              AND (:category IS NULL OR b.category_name = :category)
              AND (:publisher IS NULL OR p.name ILIKE CONCAT('%', :publisher, '%'))
              AND (:author IS NULL OR a.name ILIKE CONCAT('%', :author, '%'))
            """, nativeQuery = true)
    List<Book> findBooksDynamic(@Param("title") String title, @Param("category") String category, @Param("publisher") String publisher, @Param("author") String author);

    /* =========================
       REDUCE STOCK FROM CART
       ========================= */
    @Modifying
    @Transactional
    @Query(value = """
            UPDATE Book b
            SET stock_quantity = b.stock_quantity - cb.quantity
            FROM Cart_Book cb
            WHERE cb.cart_id = :cartId
              AND b.isbn = cb.book_isbn
            """, nativeQuery = true)
    void reduceStockFromCart(@Param("cartId") Long cartId);


    @Modifying
    @Query(value = """
                UPDATE book
                SET stock_quantity = stock_quantity + :amount
                WHERE isbn = :isbn
            """, nativeQuery = true)
    void increaseStock(@Param("isbn") String isbn, @Param("amount") int amount);

}