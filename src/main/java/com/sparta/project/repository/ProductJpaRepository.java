package com.sparta.project.repository;

import com.sparta.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product,Long> {

    // 상품명 중복 확인
    boolean existsByName(String name);
}
