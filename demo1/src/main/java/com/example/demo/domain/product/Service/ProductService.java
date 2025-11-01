package com.example.demo.domain.product.Service;

import com.example.demo.domain.category.entity.Category;
import com.example.demo.domain.category.repositry.CategoryRepository;
import com.example.demo.domain.product.dto.CreateProductRequestDto;
import com.example.demo.domain.product.dto.ProductResponseDto;
import com.example.demo.domain.product.entity.Product;
import com.example.demo.domain.product.repository.ProductReopsitory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductReopsitory productRepository;
    private final CategoryRepository categoryRepository;

    // 상품 등록
    @Transactional
    public ProductResponseDto createProduct(CreateProductRequestDto requestDto) {

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category id " + requestDto.getCategoryId() + " not found"));
        Product product = Product.builder()
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .price(requestDto.getPrice())
                .stock(requestDto.getStock())
                .category(category)
                .build();
        Product savedProduct = productRepository.save(product);
        return ProductResponseDto.from(savedProduct);
    }
}
