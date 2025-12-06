package com.sparta.msa.project_part_3.domain.cartItem.dto;

import com.sparta.msa.project_part_3.domain.product.entity.Product;
import lombok.Builder;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
public record GuestCartItem(String productName, Integer quantity, BigDecimal totalPrice)implements Serializable {
    private static final long serialVersionUID = 1L;

    public static GuestCartItem of(Product product,Integer quantity) {
        return GuestCartItem.builder()
                .productName(product.getName())
                .quantity(quantity)
                .totalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)))
                .build();
    }

    public CartItemResponse toCartItemResponse() {
        return CartItemResponse.builder()
                .productName(this.productName)
                .userName("익명의 사용자")
                .quantity(this.quantity)
                .totalPrice(this.totalPrice)
                .build();
    }}
