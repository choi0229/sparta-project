package com.sparta.project.repository.projection;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CategorySalesProjection {
    private Long categoryId;
    private String categoryName;
    private Long totalProductCount;
    private Long totalSalesCount;
    private BigDecimal totalSalesPrice;

    public CategorySalesProjection(Long categoryId, String categoryName, Long totalProductCount, Long totalSalesCount, BigDecimal totalSalesPrice) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.totalProductCount = totalProductCount;
        this.totalSalesCount = totalSalesCount;
        this.totalSalesPrice = totalSalesPrice;
    }
}
