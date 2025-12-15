package com.sparta.msa.project_part_3.domain.coupon.controller;

import com.sparta.msa.project_part_3.domain.auth.dto.response.LoginResponse;
import com.sparta.msa.project_part_3.domain.auth.service.AuthService;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponCodeRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponUserResponse;
import com.sparta.msa.project_part_3.domain.coupon.service.CouponService;
import com.sparta.msa.project_part_3.global.enums.CouponUserStatus;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;
    private final AuthService authService;

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

    @PostMapping("/issuance")
    public ApiResponse<Void> registerCoupon(@Valid @RequestBody CouponCodeRequest request){
        LoginResponse loginResponse = authService.getLoginInfo();

        couponService.registerCoupon(request, loginResponse.userId());
        return ApiResponse.ok();
    }

    @GetMapping("/users")
    public ApiResponse<List<CouponUserResponse>> findUserCoupons(){
        LoginResponse loginInfo = authService.getLoginInfo();

        List<CouponUserResponse> response = couponService.getUserCoupons(loginInfo.userId());
        return ApiResponse.ok(response);
    }

    @GetMapping("/users/status/{status}")
    public ApiResponse<List<CouponUserResponse>> findUserCoupons(@PathVariable CouponUserStatus status){
        LoginResponse loginInfo = authService.getLoginInfo();

        List<CouponUserResponse> response = couponService.getUserCouponsStatus(loginInfo.userId(), status);
        return ApiResponse.ok(response);
    }

    @PostMapping("/users/code/{couponCode}")
    public ApiResponse<Void> useCoupon(@PathVariable String couponCode){
        LoginResponse loginInfo = authService.getLoginInfo();

        couponService.useCoupon(loginInfo.userId(), couponCode);
        return ApiResponse.ok();
    }
}
