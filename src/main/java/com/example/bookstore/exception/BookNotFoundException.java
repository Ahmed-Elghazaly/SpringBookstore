package com.example.bookstore.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String isbn) {
        super("Book with ISBN " + isbn + " was not found");
    }
}