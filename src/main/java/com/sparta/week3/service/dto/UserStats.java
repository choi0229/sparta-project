package com.sparta.week3.service.dto;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class UserStats {

    private final Long userId;
    private final BigDecimal totalOrderAmount;
    private final LocalDateTime calculatedAt;
}
