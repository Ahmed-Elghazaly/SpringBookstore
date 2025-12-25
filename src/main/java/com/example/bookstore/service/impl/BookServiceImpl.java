package com.example.bookstore.service.impl;

import com.example.bookstore.dto.BookResponse;
import com.example.bookstore.dto.CreateBookRequest;
import com.example.bookstore.dto.UpdateBookRequest;
import com.example.bookstore.entity.Author;
import com.example.bookstore.entity.AuthorBook;
import com.example.bookstore.entity.Book;
import com.example.bookstore.exception.BookAlreadyExistsException;
import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.mapper.BookMapper;
import com.example.bookstore.repository.*;
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
    private final AuthorRepository authorRepository;
    private final AuthorBookRepository authorBookRepository;

    public BookServiceImpl(BookRepository bookRepository, PublisherRepository publisherRepository, CategoryRepository categoryRepository, AuthorRepository authorRepository, AuthorBookRepository authorBookRepository) {
        this.bookRepository = bookRepository;
        this.publisherRepository = publisherRepository;
        this.categoryRepository = categoryRepository;
        this.authorRepository = authorRepository;
        this.authorBookRepository = authorBookRepository;
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
        if (bookRepository.findByIsbn(request.isbn()).isPresent()) {
            throw new BookAlreadyExistsException(request.isbn());
        }

        var publisher = publisherRepository.findById(request.publisherId()).orElseThrow(() -> new RuntimeException("Publisher not found with id " + request.publisherId()));
        var category = categoryRepository.findById(request.categoryName()).orElseThrow(() -> new RuntimeException("Category not found with name " + request.categoryName()));

        var book = new Book(request.isbn(), request.title(), request.publicationYear(), request.sellingPrice(), request.stockQuantity(), request.thresholdQuantity(), publisher, category);
        var savedBook = bookRepository.save(book);

        if (request.authorNames() != null && !request.authorNames().isEmpty()) {
            for (String authorName : request.authorNames()) {
                Author author = authorRepository.findByName(authorName).orElseGet(() -> authorRepository.save(new Author(authorName)));
                AuthorBook authorBook = new AuthorBook(author, savedBook);
                authorBookRepository.save(authorBook);
            }
        }

        return BookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(String isbn, UpdateBookRequest request) {
        var book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
        if (request.title() != null) book.setTitle(request.title());
        if (request.sellingPrice() != null) book.setSellingPrice(request.sellingPrice());
        if (request.stockQuantity() != null) book.setStockQuantity(request.stockQuantity());
        if (request.thresholdQuantity() != null) book.setThresholdQuantity(request.thresholdQuantity());
        return BookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public BookResponse patchUpdateBook(String isbn, UpdateBookRequest request) {
        return updateBook(isbn, request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponse> searchBooks(String isbn, String title, String category, String publisher, String author) {
        String safeIsbn = (isbn != null && isbn.isBlank()) ? null : isbn;
        String safeTitle = (title != null && title.isBlank()) ? null : title;
        String safeCategory = (category != null && category.isBlank()) ? null : category;
        String safePublisher = (publisher != null && publisher.isBlank()) ? null : publisher;
        String safeAuthor = (author != null && author.isBlank()) ? null : author;

        return bookRepository.findBooksDynamic(safeIsbn, safeTitle, safeCategory, safePublisher, safeAuthor).stream().map(BookMapper::toResponse).toList();
    }

    @Override
    public void deleteBook(String isbn) {
        var book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
        bookRepository.delete(book);
    }
}
