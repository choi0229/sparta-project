package com.sparta.project.service.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class ProductRequestSearchDto {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private String stockStatus;

    private Long categoryId;

}
