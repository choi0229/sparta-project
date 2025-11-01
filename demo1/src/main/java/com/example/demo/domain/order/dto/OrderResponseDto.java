package com.example.demo.domain.order.dto;

import com.example.demo.domain.Status;
import com.example.demo.domain.order.entity.Order;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class OrderResponseDto {
    private Long userId;
    private BigDecimal totalPrice;
    private Status status;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderResponseDto from(Order order) {
        return builder()
                .userId(order.getUser() != null ?
                    order.getUser().getId() : null)
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
