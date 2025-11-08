package com.sparta.project.service.dto;

import com.sparta.project.PurchaseStatus;
import com.sparta.project.entity.Product;
import com.sparta.project.entity.Purchase;
import com.sparta.project.entity.PurchaseProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseResponseDto {
    Long userId;
    Long id;
    String shippingAddress;
    PurchaseStatus status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    List<PurchaseProductInfo> purchaseProducts;

    public static PurchaseResponseDto from(Purchase purchase) {
        return PurchaseResponseDto.builder()
                .id(purchase.getId())
                .userId(purchase.getUser().getId())
                .shippingAddress(purchase.getShippingAddress())
                .status(purchase.getStatus())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();
    }

    public static PurchaseResponseDto fromPurchaseProduct(Purchase purchase) {
        return PurchaseResponseDto.builder()
                .id(purchase.getId())
                .userId(purchase.getUser().getId())
                .shippingAddress(purchase.getShippingAddress())
                .status(purchase.getStatus())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .purchaseProducts(purchase.getPurchaseProducts().stream()
                        .map(pp -> PurchaseProductInfo.builder()
                                .productId(pp.getProduct().getId())
                                .productName(pp.getProduct().getName())
                                .quantity(pp.getQuantity())
                                .price(pp.getProduct().getPrice())
                                .categoryName(pp.getProduct().getCategory().getName())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class PurchaseProductInfo{
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
        private String categoryName;
    }
}
