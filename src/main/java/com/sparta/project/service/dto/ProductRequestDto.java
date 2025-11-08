package com.sparta.project.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductRequestDto {

    @NotNull(message = "상품명은 필수입니다.")
    private String name;

    @NotNull(message = "상품설명은 필수입니다.")
    private String description;

    @NotNull(message = "상품가격은 필수입니다.")
    private BigDecimal price;

    @NotNull(message = "상품 재고는 필수입니다.")
    private Integer stock;

    @NotNull(message = "상품카테고리는 필수입니다.")
    private Long categoryId;

}
