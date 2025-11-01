package com.example.demo.domain.product.dto;

import com.example.demo.domain.category.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateProductRequestDto {

    private String name;            // 상품이름

    private String description;     // 상품설명

    private BigDecimal price;       // 상품가격

    private Integer stock;          // 상품재고

    private Long categoryId;          // 상품카테고리
}
