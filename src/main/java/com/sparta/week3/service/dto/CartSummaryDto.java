package com.sparta.week3.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartSummaryDto {
    private Long userId;
    private List<CartItemDto> items;
    private Integer totalItemCount;          // 총 품목 수
    private BigDecimal totalAmount;          // 총 금액
    private BigDecimal expectedPoints;       // 예상 적립 포인트
}
