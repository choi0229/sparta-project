package com.sparta.msa.project_part_3.domain.coupon.controller;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.service.CouponService;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{couponId}")
    public ApiResponse<CouponResponse> findCouponById(@PathVariable Long couponId){
        CouponResponse response = couponService.findCouponById(couponId);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ApiResponse<Page<CouponResponse>> findCoupons(@RequestParam boolean isActive, Pageable pageable){
        Page<CouponResponse> response = couponService.findCoupons(isActive, pageable);
        return ApiResponse.ok(response);
    }

    @PostMapping
    public ApiResponse<CouponResponse> createCoupon(@Valid @RequestBody CouponRequest request){
        CouponResponse response = couponService.createCoupon(request);
        return ApiResponse.ok(response);
    }

    @DeleteMapping("/{couponId}")
    public ApiResponse<Void> deleteCoupon(@PathVariable Long couponId){
        couponService.deleteCoupon(couponId);
        return ApiResponse.ok();
    }
}
