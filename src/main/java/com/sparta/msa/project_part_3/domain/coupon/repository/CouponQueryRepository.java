package com.sparta.msa.project_part_3.domain.coupon.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.project_part_3.domain.coupon.entity.Coupon;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.msa.project_part_3.domain.coupon.entity.QCoupon.coupon;

@Repository
@RequiredArgsConstructor
public class CouponQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Coupon> findByIsActiveWithPage(Pageable pageable) {
        List<Coupon> coupons = queryFactory.selectFrom(coupon)
                .where(
                        isActive(),
                        isUsable()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(coupon.count()).from(coupon)
                .where(
                        isActive(),
                        isUsable()
                )
                .fetchOne();

        return new PageImpl<>(coupons, pageable, totalCount);
    }

    private BooleanExpression isActive(){
        LocalDateTime now = LocalDateTime.now();
        return coupon.startDate.loe(now)
                .and(coupon.endDate.goe(now))
                .and(coupon.isDeleted.eq(false));
    }

    private BooleanExpression isUsable(){
        return coupon.issueCount.lt(coupon.usageLimit);
    }

    public List<Coupon> findByIsActive(Product product) {
        return queryFactory.selectFrom(coupon)
                .where(
                        isActive(),
                        isUsable(),
                        isApplicable(product.getPrice())
                )
                .fetch();
    }

    private BooleanExpression isApplicable(BigDecimal productPrice){
        return coupon.minOrderAmount.isNull().or(coupon.minOrderAmount.loe(productPrice));
    }
}
