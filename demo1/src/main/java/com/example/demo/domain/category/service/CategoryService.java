package com.example.demo.domain.category.service;

import com.example.demo.domain.category.dto.CategoryResponseDto;
import com.example.demo.domain.category.dto.CreateCategoryRequestDto;
import com.example.demo.domain.category.dto.UpdateCategoryRequestDto;
import com.example.demo.domain.category.entity.Category;
import com.example.demo.domain.category.repositry.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto){
        Category parent = null;
        if(requestDto.getParentId() != null){
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

    // 카테고리 조회
    public List<CategoryResponseDto> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponseDto::from)
                .collect(Collectors.toList());
    }

    // 아이디로 카테고리 조회
    public CategoryResponseDto getCategorybyId(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category " + id + "가 없습니다"));
        return CategoryResponseDto.from(category);
    }

    // 카테고리 수정
    @Transactional
    public CategoryResponseDto updateCategory(Long id, UpdateCategoryRequestDto requestDto){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category " + id + "가 없습니다"));

        if(requestDto.getName() != null){
            category.updateName(requestDto.getName());
        }

        if(requestDto.getDescription() != null){
            category.updateDescription(requestDto.getDescription());
        }
        // Category updatedCategory = categoryRepository.save(category);
        return CategoryResponseDto.from(category);
    }
}
