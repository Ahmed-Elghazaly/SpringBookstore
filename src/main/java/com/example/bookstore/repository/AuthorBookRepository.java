package com.example.bookstore.repository;

import com.example.bookstore.entity.AuthorBook;
import com.example.bookstore.entity.AuthorBookId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorBookRepository extends JpaRepository<AuthorBook, AuthorBookId> {
}
