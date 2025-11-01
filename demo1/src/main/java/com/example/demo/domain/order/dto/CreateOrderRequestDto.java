package com.example.demo.domain.order.dto;

import com.example.demo.domain.Status;
import com.example.demo.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class CreateOrderRequestDto {
    private Long userId;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private Status status;
}
