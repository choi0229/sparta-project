package com.sparta.msa.project_part_3.domain.cartItem.dto;

import com.sparta.msa.project_part_3.domain.cartItem.entity.CartItem;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CartItemResponse(String productName, String userName, Integer quantity, BigDecimal totalPrice) {

    public static CartItemResponse from(CartItem cartItem) {
        return CartItemResponse.builder()
                .productName(cartItem.getProduct().getName())
                .userName(cartItem.getUser().getName())
                .quantity(cartItem.getQuantity())
                .totalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .build();
    }
}
