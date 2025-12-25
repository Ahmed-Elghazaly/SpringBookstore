package com.example.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "author_book")
public class AuthorBook {

    @EmbeddedId
    private AuthorBookId id;

    @ManyToOne(optional = false)
    @MapsId("authorId")
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne(optional = false)
    @MapsId("bookIsbn")
    @JoinColumn(name = "book_isbn")
    private Book book;

    protected AuthorBook() {
    }

    public AuthorBook(Author author, Book book) {
        this.author = author;
        this.book = book;
        this.id = new AuthorBookId(author.getAuthorId(), book.getIsbn());
    }

    public AuthorBookId getId() {
        return id;
    }

    public Author getAuthor() {
        return author;
    }

    public Book getBook() {
        return book;
    }
}
