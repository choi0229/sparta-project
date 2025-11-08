package com.sparta.project.repository;

import com.sparta.project.entity.PurchaseProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseProductJpaRepository extends JpaRepository<PurchaseProduct, Long> {
}
