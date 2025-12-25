package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.entity.Book;

import java.util.List;

public final class BookMapper {

    private BookMapper() {
        // Prevent instantiation
    }

    public static BookResponse toResponse(Book book) {
        if (book == null) {
            return null;
        }

        List<String> authors = book.getAuthorBooks() == null
                ? List.of()
                : book.getAuthorBooks()
                .stream()
                .map(ab -> ab.getAuthor().getName())
                .toList();

        return new BookResponse(book.getIsbn(), book.getTitle(), book.getPublicationYear(), book.getSellingPrice(), book.getStockQuantity(), book.getThresholdQuantity(), book.getCategory().getCategoryName(), book.getPublisher().getName(), authors);
    }
}