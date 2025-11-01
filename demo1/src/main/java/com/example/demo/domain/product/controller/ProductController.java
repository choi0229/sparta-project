package com.example.demo.domain.product.controller;

import com.example.demo.domain.product.Service.ProductService;
import com.example.demo.domain.product.dto.CreateProductRequestDto;
import com.example.demo.domain.product.dto.ProductResponseDto;
import com.example.demo.domain.product.entity.Product;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductRequestDto product){
        ProductResponseDto res = productService.createProduct(product);
        return ResponseEntity.ok(res);
    }

    // 상품 조회
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<ProductResponseDto> res = productService.getAllProducts();
        return ResponseEntity.ok(res);
    }

    // 단일 상품 조회
    @GetMapping("{/id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id){
        ProductResponseDto res = productService.getProductById(id);
        return ResponseEntity.ok(res);
    }

    // 상품 수정
    @PutMapping("{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id, @Valid @RequestBody CreateProductRequestDto product){
        ProductResponseDto res = productService.updateProduct(id, product);
        return ResponseEntity.ok(res);
    }
}
