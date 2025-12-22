package com.example.bookstore.mapper;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.entity.Book;

public final class BookMapper {

    private BookMapper() {
        // Prevent instantiation
    }

    public static BookResponse toResponse(Book book) {
        if (book == null) {
            return null;
        }

        return new BookResponse(
                book.getIsbn(),
                book.getTitle(),
                book.getSellingPrice(),
                book.getStockQuantity()
        );
    }
}
