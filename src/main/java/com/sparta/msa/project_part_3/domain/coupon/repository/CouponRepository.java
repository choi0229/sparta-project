package com.sparta.msa.project_part_3.domain.coupon.repository;

import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
