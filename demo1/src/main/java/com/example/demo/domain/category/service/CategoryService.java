package com.example.demo.domain.category.service;

import com.example.demo.domain.category.dto.CategoryResponseDto;
import com.example.demo.domain.category.dto.CreateCategoryRequestDto;
import com.example.demo.domain.category.entity.Category;
import com.example.demo.domain.category.repositry.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto){
        Category parent = null;
        if(parent != null){
            parent = categoryRepository.findById(requestDto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Category id " + requestDto.getParentId() + " not found"));
        }

        Category category = Category.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .parent(parent)
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponseDto.from(savedCategory);
    }
}
