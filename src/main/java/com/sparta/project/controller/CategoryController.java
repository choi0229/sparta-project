package com.sparta.project.controller;

import com.sparta.project.common.ApiResponse;
import com.sparta.project.repository.projection.CategorySalesProjection;
import com.sparta.project.repository.projection.ProductSalesProjection;
import com.sparta.project.service.CategoryService;
import com.sparta.project.service.dto.CategoryRequestDto;
import com.sparta.project.service.dto.CategoryResponseDto;
import com.sparta.project.service.dto.ProductResponseDto;
import com.sparta.project.service.dto.ResponseCaategoryDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createCategory(@Valid @RequestBody CategoryRequestDto request){
        categoryService.createCategory(request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequestDto request){
        categoryService.updateCategory(id, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/tree")
    public ResponseEntity<ApiResponse<List<CategoryResponseDto>>> getCategoryTree(){
        List<CategoryResponseDto> response = categoryService.getCategoryTree();
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/{id}/top-products")
    public ResponseEntity<ApiResponse<List<ProductSalesProjection>>> getTopProducts(@PathVariable Long id){
        List<ProductSalesProjection> response = categoryService.getTopProducts(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/top-categories")
    public ResponseEntity<ApiResponse<List<CategorySalesProjection>>> getTopCategories(){
        int limit = 10;
        List<CategorySalesProjection> response = categoryService.getTopCategories(limit);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
