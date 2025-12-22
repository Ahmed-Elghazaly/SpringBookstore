package com.example.bookstore.service;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.CreateBookRequest;
import com.example.bookstore.dto.UpdateBookRequest;

import java.util.List;

public interface BookService {

    List<BookResponse> getAllBooks();

    BookResponse getBookByIsbn(String isbn);

    BookResponse createBook(CreateBookRequest request);

    BookResponse updateBook(String isbn, UpdateBookRequest request);

    BookResponse patchUpdateBook(String isbn, UpdateBookRequest request);

    List<BookResponse> searchBooks(
            String title,
            String category,
            String author,
            String publisher
    );

    void deleteBook(String isbn);
}