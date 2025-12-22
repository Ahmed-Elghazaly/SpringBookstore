package com.example.bookstore.repository;

import com.example.bookstore.entity.PublisherOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublisherOrderRepository extends JpaRepository<PublisherOrder, Long> {
}