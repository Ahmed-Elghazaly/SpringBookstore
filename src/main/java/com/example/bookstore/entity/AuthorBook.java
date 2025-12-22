package com.example.bookstore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Author_Book")
public class AuthorBook {

    @EmbeddedId
    private AuthorBookId id;

    @ManyToOne(optional = false)
    @MapsId("authorId")
    private Author author;

    @ManyToOne(optional = false)
    @MapsId("bookIsbn")
    private Book book;

    protected AuthorBook() {
    }

    public AuthorBook(Author author, Book book) {
        this.author = author;
        this.book = book;
        this.id = new AuthorBookId(author.getAuthorId(), book.getIsbn());
    }

    public Author getAuthor() {
        return author;
    }

    public Book getBook() {
        return book;
    }
}
