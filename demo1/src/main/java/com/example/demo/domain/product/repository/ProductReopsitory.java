package com.example.demo.domain.product.repository;

import com.example.demo.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductReopsitory extends JpaRepository<Product, Long> {
}
