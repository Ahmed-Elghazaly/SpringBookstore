package com.example.bookstore.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "Author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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