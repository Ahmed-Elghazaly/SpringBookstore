package com.example.bookstore.repository;

import com.example.bookstore.entity.ShoppingCart;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface ShoppingCartRepository extends Repository<ShoppingCart, Long> {

    @Query(value = """
            SELECT cart_id
            FROM shoppingcart
            WHERE customer_id = :customerId
            """, nativeQuery = true)
    Optional<Long> findCartIdByCustomerId(@Param("customerId") Long customerId);


    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO shoppingcart (customer_id)
            VALUES (:customerId)
            RETURNING cart_id
            """, nativeQuery = true)
    Long createCart(@Param("customerId") Long customerId);
}
