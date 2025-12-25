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

    Optional<Book> findByIsbn(String isbn);

    @Query(value = """
            SELECT DISTINCT b.*
            FROM book b
            LEFT JOIN publisher p ON b.publisher_id = p.publisher_id
            LEFT JOIN author_book ab ON b.isbn = ab.book_isbn
            LEFT JOIN author a ON ab.author_id = a.author_id
            WHERE 
              (:isbn IS NULL OR b.isbn ILIKE CONCAT('%', :isbn, '%'))
              AND (:title IS NULL OR b.title ILIKE CONCAT('%', :title, '%'))
              AND (:category IS NULL OR b.category_name ILIKE :category) 
              AND (:publisher IS NULL OR p.name ILIKE CONCAT('%', :publisher, '%'))
              AND (:author IS NULL OR a.name ILIKE CONCAT('%', :author, '%'))
            """, nativeQuery = true)
    List<Book> findBooksDynamic(
            @Param("isbn") String isbn,
            @Param("title") String title,
            @Param("category") String category,
            @Param("publisher") String publisher,
            @Param("author") String author
    );

    @Modifying
    @Transactional
    @Query(value = "UPDATE book SET stock_quantity = stock_quantity - :amount WHERE isbn = :isbn", nativeQuery = true)
    void reduceStock(@Param("isbn") String isbn, @Param("amount") int amount);

    @Modifying
    @Transactional
    @Query(value = "UPDATE book SET stock_quantity = stock_quantity + :amount WHERE isbn = :isbn", nativeQuery = true)
    void increaseStock(@Param("isbn") String isbn, @Param("amount") int amount);
}
