package com.sparta.project.service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PurchaseRequestDto {

    @NotNull(message = "유저 아이디는 필수입니다.")
    private Long userId;

    @NotNull(message = "상품 아이디는 필수입니다.")
    private Long productId;

    @NotNull(message = "수량 필수입니다.")
    private Integer quantity;

    @NotNull(message = "배송지는 필수입니다.")
    private String shippingAddress;


}
