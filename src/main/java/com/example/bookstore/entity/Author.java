package com.example.bookstore.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long authorId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "author")
    private List<AuthorBook> authorBooks;

    protected Author() {
    }

    public Author(String name) {
        this.name = name;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public String getName() {
        return name;
    }

    public List<AuthorBook> getAuthorBooks() {
        return authorBooks;
    }
}
