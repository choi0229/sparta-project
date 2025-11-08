package com.sparta.project.service;

import com.sparta.project.PurchaseStatus;
import com.sparta.project.common.ServiceException;
import com.sparta.project.common.ServiceExceptionCode;
import com.sparta.project.entity.Category;
import com.sparta.project.entity.Product;
import com.sparta.project.repository.CategoryQueryRepository;
import com.sparta.project.repository.ProductJpaRepository;
import com.sparta.project.repository.ProductQueryRepository;
import com.sparta.project.service.dto.ProductRequestSearchDto;
import com.sparta.project.service.dto.ProductRequestDto;
import com.sparta.project.service.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductJpaRepository productRepository;
    private final CategoryQueryRepository categoryRepository;
    private final ProductQueryRepository productQueryRepository;

    @Transactional
    public Long createProduct(ProductRequestDto request) {
        Category category = categoryRepository.findById(request.getCategoryId());
        if(category == null){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY);
        }

        if(productRepository.existsByName(request.getName())){
            throw new ServiceException(ServiceExceptionCode.ALREADY_EXISTS_PRODUCT);
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(category)
                .build();

        Product savedProduct = productRepository.save(product);

        // 저장된 상품 queryDsl로 조회
        Long productId = productQueryRepository.findIdByName(savedProduct.getName());
        return productId;
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto request){
        Product product = productQueryRepository.findById(id);
        if(product == null){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
        }

        Category category = categoryRepository.findById(request.getCategoryId());
        if(category == null){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_CATEGORY);
        }

        product.updateProduct(
                category,
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock()
        );

        return ProductResponseDto.from(product);
    }

    @Transactional
    public void deleteProduct(Long id){
        boolean productStatus = productQueryRepository.hasCompletedPurchase(id, PurchaseStatus.COMPLETED);

        if(productStatus){
            throw new ServiceException(ServiceExceptionCode.DELETE_NOT_ALLOWED);
        }

        productRepository.deleteById(id);
    }

    @Transactional
    public ProductResponseDto getProductById(Long id){
        Product product = productQueryRepository.findById(id);
        if(product == null){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
        }
        return ProductResponseDto.from(product);
    }

    @Transactional
    public Page<ProductResponseDto> searchProducts(ProductRequestSearchDto request, Pageable pageable){
        Page<Product> products = productQueryRepository.searchProducts(request, pageable);
        return products.map(ProductResponseDto::from);
    }

}
