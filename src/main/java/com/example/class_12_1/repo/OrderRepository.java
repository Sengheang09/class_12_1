package com.example.class_12_1.repo;

import com.example.class_12_1.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);

//    Optional<Order> findById(Long id); order
    @Query("""
        SELECT DISTINCT o
            FROM Order o
            JOIN FETCH o.user
            JOIN FETCH orderItem i
            JOIN FETCH i.product
            WHERE o.id = :id
    """)
    Optional<Order> findByIdWithDetails(Long id);

    @Query("""
        SELECT DISTINCT o
            FROM Order o
            JOIN FETCH o.user
            JOIN FETCH orderItem i
            JOIN FETCH i.product
    """)
    List<Order> findAllWithDetails();

}
