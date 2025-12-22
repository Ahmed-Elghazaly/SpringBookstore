package com.example.bookstore.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AuthorBookId implements Serializable {

    private Long authorId;
    private String bookIsbn;

    protected AuthorBookId() {
        // required by JPA
    }

    public AuthorBookId(Long authorId, String bookIsbn) {
        this.authorId = authorId;
        this.bookIsbn = bookIsbn;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorBookId that)) return false;
        return Objects.equals(authorId, that.authorId)
                && Objects.equals(bookIsbn, that.bookIsbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, bookIsbn);
    }
}
