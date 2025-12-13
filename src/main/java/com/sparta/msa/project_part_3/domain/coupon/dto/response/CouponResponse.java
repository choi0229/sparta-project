package com.sparta.msa.project_part_3.domain.coupon.dto.response;

import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.global.enums.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponResponse(
        Long id,
        String couponName,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minOrderAmount,
        BigDecimal maxDiscountAmount,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer remainingCount,
        Boolean isActive
) {

    public static CouponResponse from(Coupon coupon){
        return CouponResponse.builder()
                .id(coupon.getId())
                .couponName(coupon.getCouponName())
                .discountType(coupon.getDiscountType())
                .discountValue(coupon.getDiscountValue())
                .minOrderAmount(coupon.getMinOrderAmount())
                .maxDiscountAmount(coupon.getMaxDiscountAmount())
                .startDate(coupon.getStartDate())
                .endDate(coupon.getEndDate())
                .remainingCount(coupon.getUsageLimit() - coupon.getIssueCount())
                .isActive(isActive(coupon))
                .build();
    }

    private static Boolean isActive(Coupon coupon){
        LocalDateTime now = LocalDateTime.now();
        return !coupon.getIsDeleted()
                && now.isAfter(coupon.getStartDate())
                && now.isBefore(coupon.getEndDate())
                && coupon.getUsageLimit() > coupon.getIssueCount();
    }
}
