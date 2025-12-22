package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.CreateBookRequest;
import com.example.bookstore.dto.UpdateBookRequest;
import com.example.bookstore.exception.BookAlreadyExistsException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CategoryRepository;
import com.example.bookstore.repository.PublisherRepository;
import com.example.bookstore.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final CategoryRepository categoryRepository;

    public BookServiceImpl(BookRepository bookRepository, PublisherRepository publisherRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(BookMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(BookMapper::toResponse).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Override
    public BookResponse createBook(CreateBookRequest request) {
        // Business rule: ISBN must be unique
        if (bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new BookAlreadyExistsException(request.isbn());
        }

        // Fetch required Publisher
        var publisher = publisherRepository.findById(request.publisherId()).orElseThrow(() -> new RuntimeException("Publisher not found with id " + request.publisherId()));

        // Fetch required Category
        var category = categoryRepository.findById(request.categoryName()).orElseThrow(() -> new RuntimeException("Category not found with name " + request.categoryName()));

        // Create Book entity
        var book = new com.example.bookstore.entity.Book(request.isbn(), request.title(), request.sellingPrice(), request.stockQuantity(), request.thresholdQuantity(), publisher, category);

        // Persist
        var savedBook = bookRepository.save(book);

        return BookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(String isbn, UpdateBookRequest request) {

        // 1. Fetch existing book or fail
        var book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));

        // 2. Apply partial updates (only if provided)
        if (request.title() != null) {
            book.setTitle(request.title());
        }

        if (request.sellingPrice() != null) {
            book.setSellingPrice(request.sellingPrice());
        }

        if (request.stockQuantity() != null) {
            book.setStockQuantity(request.stockQuantity());
        }

        if (request.thresholdQuantity() != null) {
            book.setThresholdQuantity(request.thresholdQuantity());
        }

        // 3. Persist updated entity
        var savedBook = bookRepository.save(book);

        // 4. Map to response
        return BookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse patchUpdateBook(String isbn, UpdateBookRequest request) {
        return updateBook(isbn, request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(String title, String category, String author, String publisher) {
        var books = bookRepository.findBooksDynamic(title, category, publisher, author);

        return books.stream().map(BookMapper::toResponse).toList();
    }

    @Override
    public void deleteBook(String isbn) {

        var book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));

        bookRepository.delete(book);
    }
}
