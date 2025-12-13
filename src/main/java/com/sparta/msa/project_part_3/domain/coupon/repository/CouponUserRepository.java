package com.sparta.msa.project_part_3.domain.coupon.repository;

import com.sparta.msa.project_part_3.domain.coupon.entity.CouponUser;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cu FROM CouponUser cu JOIN FETCH cu.coupon c WHERE cu.code = :couponCode")
    Optional<CouponUser> findByCodeWithCoupon(String code);
}
