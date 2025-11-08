package com.sparta.project.repository;

import com.sparta.project.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseJpaRepository extends JpaRepository<Purchase, Long> {
}
