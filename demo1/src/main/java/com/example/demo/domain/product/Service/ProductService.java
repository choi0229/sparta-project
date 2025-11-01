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
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<ProductResponseDto> getAllProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(@PathVariable Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Product"+ id + "가 없습니다"));
        return ProductResponseDto.from(product);
    }

    // 상품 수정
    @Transactional
    public ProductResponseDto updateProduct(@PathVariable Long id, CreateProductRequestDto requestDto){
        Product product =  productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product"+ id + "가 없습니다"));

        Category category = categoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category id " + requestDto.getCategoryId() + "가 없습니다"));

        String name = requestDto.getName();
        String description = requestDto.getDescription();
        BigDecimal price = requestDto.getPrice();
        Integer stock = requestDto.getStock();
        product.updateProduct(name, description, price, stock, category);

        return ProductResponseDto.from(product);
    }
}
