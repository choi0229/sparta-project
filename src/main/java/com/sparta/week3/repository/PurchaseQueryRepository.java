package com.sparta.week3.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

import static com.sparta.week3.entity.QPurchase.purchase;

@Repository
@RequiredArgsConstructor
public class PurchaseQueryRepository {
    private final JPAQueryFactory queryFactory;

    public BigDecimal sumPurchaseAmountByUserId(Long userId) {
        BigDecimal result = queryFactory
                .select(purchase.totalPrice.sum())
                .from(purchase)
                .where(userId != null ? purchase.user.id.eq(userId) : null)
                .groupBy(purchase.user.id)
                .fetchOne();

        return result != null ? result : BigDecimal.ZERO;
    }
}

