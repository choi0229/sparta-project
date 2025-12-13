package com.sparta.msa.project_part_3.domain.product.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ProductMaxDiscountResponse(
        Long productId,
        String productName,
        BigDecimal maxDiscountRate,
        BigDecimal maxDiscountAmount,
        Long couponId,
        String couponName,
        BigDecimal finalPrice
) {
}
