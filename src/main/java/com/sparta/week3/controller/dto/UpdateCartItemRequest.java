package com.sparta.week3.controller.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateCartItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;
}
