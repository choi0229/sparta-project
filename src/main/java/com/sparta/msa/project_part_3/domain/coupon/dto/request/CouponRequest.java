package com.sparta.msa.project_part_3.domain.coupon.dto.request;

import com.sparta.msa.project_part_3.global.enums.DiscountType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponRequest(
        @NotNull String couponName,
        @NotNull DiscountType discountType,
        @NotNull BigDecimal discountValue,
        @NotNull BigDecimal minOrderAmount,
        @NotNull BigDecimal maxDiscountAmount,
        @NotNull LocalDateTime startDate,
        @NotNull LocalDateTime endDate,
        @NotNull Integer usageLimit
) {

    @AssertTrue(message = "종료일은 시작일 이후여야 합니다")
    public boolean isValidDateRange(){
        if(startDate == null || endDate == null){
            return true;
        }
        return endDate.isAfter(startDate);
    }

    @AssertTrue(message = "정률 할인은 0~100 사이여야 합니다")
    public boolean isValidPercentage(){
        if(discountType == DiscountType.PERCENTAGE && discountValue != null){
            return discountValue.compareTo(BigDecimal.ZERO) > 0
                    && discountValue.compareTo(BigDecimal.valueOf(100)) <= 0;
        }
        return true;
    }

    @AssertTrue(message = "정액 할인 시 최소 1000원 이상이어야 합니다..")
    public boolean isValidFixed(){
        if(discountType == DiscountType.FIXED && discountValue != null){
            return discountValue.compareTo(BigDecimal.valueOf(1000)) >= 0;
        }
        return true;
    }

}
