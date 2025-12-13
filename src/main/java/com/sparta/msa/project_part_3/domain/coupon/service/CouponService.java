package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponQueryRepository;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponQueryRepository couponQueryRepository;

    @Transactional(readOnly = true)
    public CouponResponse findCouponById(Long id){
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        return CouponResponse.from(coupon);
    }

    @Transactional(readOnly = true)
    public Page<CouponResponse> findCoupons(Boolean isActive, Pageable pageable){
        Page<Coupon> coupons;

        if (isActive) {
            coupons = couponQueryRepository.findByIsActiveWithPage(pageable);
        }else{
            coupons = couponRepository.findAll(pageable);
        }

        Page<CouponResponse> response = coupons.map(CouponResponse::from);

        return response;
    }

    @Transactional
    public CouponResponse createCoupon(CouponRequest request){
        Coupon coupon = Coupon.builder()
                .couponName(request.couponName())
                .discountType(request.discountType())
                .discountValue(request.discountValue())
                .minOrderAmount(request.minOrderAmount())
                .maxDiscountAmount(request.maxDiscountAmount())
                .startDate(request.startDate())
                .endDate(request.endDate())
                .usageLimit(request.usageLimit())
                .build();

        couponRepository.save(coupon);
        return CouponResponse.from(coupon);
    }

    @Transactional
    public void deleteCoupon(Long couponId){
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        coupon.setDelete();
    }
}
