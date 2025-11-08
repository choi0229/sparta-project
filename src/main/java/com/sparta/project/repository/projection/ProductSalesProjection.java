package com.sparta.project.repository.projection;

import com.sparta.project.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
public class ProductSalesProjection {
    private Long productId;
    private String productName;
    private Long categoryId;
    private Long salesCount;
    private BigDecimal totalSales;

    public ProductSalesProjection(Long productId, String productName, Long categoryId, Long salesCount, BigDecimal totalSales) {
        this.productId = productId;
        this.productName = productName;
        this.categoryId = categoryId;
        this.salesCount = salesCount;
        this.totalSales = totalSales;
    }
}
