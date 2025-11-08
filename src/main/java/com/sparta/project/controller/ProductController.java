package com.sparta.project.controller;

import com.sparta.project.common.ApiResponse;
import com.sparta.project.entity.Product;
import com.sparta.project.service.ProductService;
import com.sparta.project.service.dto.ProductRequestDto;
import com.sparta.project.service.dto.ProductRequestSearchDto;
import com.sparta.project.service.dto.ProductResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createProduct(@Valid @RequestBody ProductRequestDto request){
        Long savedId = productService.createProduct(request);
        return ApiResponse.created(savedId);
    }

    // 상품 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id, @Valid @RequestBody ProductRequestDto request
    ) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, request);
        return ResponseEntity.ok(ApiResponse.success(updatedProduct));
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    // 단일 상품 조회
    @GetMapping("/{id}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long id){
        ProductResponseDto response = productService.getProductById(id);
        return ApiResponse.success(response);
    }

    // 전체 상품 조회 (검색조건 : 카테고리, 가격범위, 재고상태(재고없음 포함))
    @GetMapping
    public ApiResponse<Page<ProductResponseDto>> searchProducts(
            ProductRequestSearchDto request,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ){
        Page<ProductResponseDto> response = productService.searchProducts(request, pageable);
        return ApiResponse.success(response);
    }


}
