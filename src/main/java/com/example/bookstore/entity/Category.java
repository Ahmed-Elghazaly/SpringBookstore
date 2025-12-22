package com.example.bookstore.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "Category")
public class Category {

    @Id
    private String categoryName;

    @OneToMany(mappedBy = "category")
    private List<Book> books;

    protected Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Book> getBooks() {
        return books;
    }
}