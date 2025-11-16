package com.sparta.week3.repository;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.week3.PointTransactionType;
import com.sparta.week3.entity.PointTransaction;
import com.sparta.week3.entity.QPointTransaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.week3.entity.QPointTransaction.pointTransaction;

@Repository
@RequiredArgsConstructor
public class PointTransactionQueryRepository {
    private JPAQueryFactory queryFactory;

    public List<PointTransaction> findExpiredTransactions(LocalDateTime now) {
        QPointTransaction pt = QPointTransaction.pointTransaction;
        QPointTransaction sub = new QPointTransaction("sub");

        return queryFactory
                .selectFrom(pt)
                .where(
                        pt.type.eq(PointTransactionType.EARN),
                        pt.expiresAt.loe(now),
                        pt.expiresAt.isNotNull(),
                        JPAExpressions
                                .selectOne()
                                .from(sub)
                                .where(
                                        sub.type.eq(PointTransactionType.EXTINCTION),
                                        sub.pointWallet.id.eq(pt.pointWallet.id),
                                        sub.purchase.id.eq(pt.purchase.id)
                                )
                                .notExists()
                )
                .fetch();
    }
}
