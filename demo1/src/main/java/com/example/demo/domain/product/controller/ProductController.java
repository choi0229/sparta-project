package com.example.demo.domain.product.controller;

import com.example.demo.domain.product.Service.ProductService;
import com.example.demo.domain.product.dto.CreateProductRequestDto;
import com.example.demo.domain.product.dto.ProductResponseDto;
import com.example.demo.domain.product.entity.Product;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody CreateProductRequestDto product){
        ProductResponseDto rep = productService.createProduct(product);
        return ResponseEntity.ok(rep);
    }
}
