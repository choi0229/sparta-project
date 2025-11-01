package com.example.demo.domain.category.controller;

import com.example.demo.domain.category.dto.CategoryResponseDto;
import com.example.demo.domain.category.dto.CreateCategoryRequestDto;
import com.example.demo.domain.category.service.CategoryService;
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
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponseDto> createCategory(@Valid @RequestBody CreateCategoryRequestDto category){
        CategoryResponseDto res = categoryService.createCategory(category);
        return ResponseEntity.ok(res);
    }

}
