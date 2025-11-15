package com.sparta.week3;

import java.math.BigDecimal;

public enum CategoryGrade {
    NORMAL(BigDecimal.valueOf(0.1)),
    PREMIUM(BigDecimal.valueOf(0.15)),
    EVENT(BigDecimal.valueOf(0.2));

    private final BigDecimal pointWeight;

    CategoryGrade(BigDecimal pointWeight) {
        this.pointWeight = pointWeight;
    }

    public BigDecimal getPointWeight() {
        return pointWeight;
    }
}
