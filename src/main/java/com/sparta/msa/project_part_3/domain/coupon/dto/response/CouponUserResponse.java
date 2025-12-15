package com.sparta.msa.project_part_3.domain.coupon.dto.response;

import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import com.sparta.msa.project_part_3.global.enums.CouponUserStatus;
import com.sparta.msa.project_part_3.global.enums.DiscountType;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponUserResponse(
        String couponName,
        DiscountType discountType,
        BigDecimal discountValue,
        BigDecimal minOrderAmount,
        BigDecimal maxDiscountAmount,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String code,
        CouponUserStatus status
) {

    public static CouponUserResponse from(CouponUser couponUser){
        return CouponUserResponse.builder()
                .couponName(couponUser.getCoupon().getCouponName())
                .discountType(couponUser.getCoupon().getDiscountType())
                .discountValue(couponUser.getCoupon().getDiscountValue())
                .minOrderAmount(couponUser.getCoupon().getMinOrderAmount())
                .maxDiscountAmount(couponUser.getCoupon().getMaxDiscountAmount())
                .startDate(couponUser.getCoupon().getStartDate())
                .endDate(couponUser.getCoupon().getEndDate())
                .code(couponUser.getCode())
                .status(couponUser.getStatus())
                .build();
    }
}
