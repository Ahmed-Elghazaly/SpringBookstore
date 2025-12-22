package com.example.bookstore.controller;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.CreateBookRequest;
import com.example.bookstore.dto.UpdateBookRequest;
import com.example.bookstore.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {


    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping
    public List<BookResponse> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    public BookResponse getBookByIsbn(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponse createBook(@RequestBody CreateBookRequest request) {
        return bookService.createBook(request);
    }

    @PutMapping("/{isbn}")
    public BookResponse updateBook(@PathVariable String isbn, @RequestBody UpdateBookRequest request) {
        return bookService.updateBook(isbn, request);
    }

    @PatchMapping("/{isbn}")
    public BookResponse patchUpdateBook(@PathVariable String isbn, @RequestBody UpdateBookRequest request) {
        return bookService.patchUpdateBook(isbn, request);
    }

    @GetMapping("/search")
    public List<BookResponse> searchBooks(@RequestParam(required = false) String title, @RequestParam(required = false) String category, @RequestParam(required = false) String author, @RequestParam(required = false) String publisher) {
        return bookService.searchBooks(title, category, author, publisher);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String isbn) {
        bookService.deleteBook(isbn);
    }
}