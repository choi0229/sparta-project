package com.sparta.msa.project_part_3.domain.coupon.service;

import com.sparta.msa.project_part_3.domain.auth.service.AuthService;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponCodeRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.request.CouponRequest;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponResponse;
import com.sparta.msa.project_part_3.domain.coupon.dto.response.CouponUserResponse;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponQueryRepository;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponRepository;
import com.sparta.msa.project_part_3.domain.coupon.repository.CouponUserRepository;
import com.sparta.msa.project_part_3.domain.user.entity.User;
import com.sparta.msa.project_part_3.domain.user.repository.UserRepository;
import com.sparta.msa.project_part_3.global.enums.CouponUserStatus;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponQueryRepository couponQueryRepository;
    private final CouponUserRepository couponUserRepository;
    private final UserRepository userRepository;

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

        Coupon savedCoupon = couponRepository.save(coupon);

        generatedCode(savedCoupon);
        return CouponResponse.from(coupon);
    }

    private String generateUniqueCode(){
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }

    private void generatedCode(Coupon coupon){
        List<CouponUser> couponUsers = new ArrayList<>();
        Set<String> generatedCodes = new HashSet<>();

        int attempts = 0;
        int maxAttempts = coupon.getUsageLimit();
        while(attempts < maxAttempts){
            String code = generateUniqueCode();

            if(generatedCodes.add(code)){
                CouponUser couponUser = CouponUser.builder()
                        .coupon(coupon)
                        .user(null)
                        .code(code)
                        .status(CouponUserStatus.PENDING)
                        .build();

                couponUsers.add(couponUser);
            }
            attempts++;
        }

        if(generatedCodes.size() < maxAttempts){
            throw new DomainException(DomainExceptionCode.FAILED_CREATE_COUPON_CODE);
        }
        couponUserRepository.saveAll(couponUsers);
    }

    @Transactional
    public void deleteCoupon(Long couponId){
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        coupon.setDelete();
    }

    @Transactional
    public void registerCoupon(CouponCodeRequest request, Long userId){

        CouponUser couponUser = couponUserRepository.findByCodeWithCoupon(request.couponCode())
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        User user = userRepository.findById(userId)
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_USER));

        validateCouponUser(couponUser);
        if(!couponUser.getStatus().equals(CouponUserStatus.PENDING)){
            throw new DomainException(DomainExceptionCode.ISSUED_COUPON);
        }

        couponUser.getCoupon().increaseIssueCount();
        couponUser.updateStatus(CouponUserStatus.ISSUED, user);
    }

    private void validateCouponUser(CouponUser couponUser){
        LocalDateTime now = LocalDateTime.now();

        if(couponUser.getCoupon().getIsDeleted()){
            throw new DomainException(DomainExceptionCode.DELETED_COUPON);
        }

        if(now.isBefore(couponUser.getCoupon().getStartDate()) || now.isAfter(couponUser.getCoupon().getEndDate())){
            throw new DomainException(DomainExceptionCode.INVALID_DATE);
        }

        if(couponUser.getCoupon().getIssueCount() >= couponUser.getCoupon().getUsageLimit()){
            throw new DomainException(DomainExceptionCode.EXHAUSTED_COUPON);
        }
    }

    public List<CouponUserResponse> getUserCoupons(Long userId){
        List<CouponUser> coupons = couponUserRepository.findByUserId(userId);

        return coupons.stream()
                .map(CouponUserResponse::from)
                .toList();
    }

    public List<CouponUserResponse> getUserCouponsStatus(Long userId, CouponUserStatus status){
        List<CouponUser> coupons = couponUserRepository.findByUserIdAndStatus(userId, status);

        return coupons.stream()
                .map(CouponUserResponse::from)
                .toList();
    }

    @Transactional
    public void useCoupon(Long userId, String couponCode){
        CouponUser couponUser = couponUserRepository.findByUserIdAndCouponCode(userId, couponCode)
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_COUPON));

        validateCouponUser(couponUser);
        if(!couponUser.getStatus().equals(CouponUserStatus.ISSUED)){
            throw new DomainException(DomainExceptionCode.ISSUED_COUPON);
        }

        couponUser.getCoupon().increaseUsedCount();
        couponUser.updateStatus(CouponUserStatus.USED, couponUser.getUser());
    }
}
