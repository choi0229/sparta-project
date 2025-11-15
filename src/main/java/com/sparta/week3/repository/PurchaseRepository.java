package com.sparta.week3.repository;

import com.sparta.week3.entity.Purchase;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase,Long> {
}
