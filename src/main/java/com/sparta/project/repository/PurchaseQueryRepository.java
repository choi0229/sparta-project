package com.sparta.project.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.project.PurchaseStatus;
import com.sparta.project.entity.Product;
import com.sparta.project.entity.Purchase;
import com.sparta.project.entity.PurchaseProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.sparta.project.entity.QProduct.product;
import static com.sparta.project.entity.QPurchase.purchase;
import static com.sparta.project.entity.QPurchaseProduct.purchaseProduct;

@Repository
public class PurchaseQueryRepository {

    private final JPAQueryFactory queryFactory;

    public PurchaseQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Purchase getPurchaseProducts(Long id){
        return queryFactory
                .selectFrom(purchase)
                .distinct()
                .leftJoin(purchase.purchaseProducts, purchaseProduct).fetchJoin()
                .leftJoin(purchaseProduct.product, product).fetchJoin()
                .where(purchase.id.eq(id))
                .fetchOne();
    }

    public List<PurchaseProduct> findProductById(Long purchaseId){
        return queryFactory.selectFrom(purchaseProduct)
                .leftJoin(purchaseProduct.product, product).fetchJoin()
                .where(purchaseProduct.purchase.id.eq(purchaseId))
                .fetch();
    }

    public Page<Purchase> getSearchPurchase(
            Long userId,
            PurchaseStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ){
        List<Purchase> contente = queryFactory.selectFrom(purchase)
                .where(
                        purchaseByUserId(userId),
                        purchaseByStatus(status),
                        purchaseGoeCreatedAt(startDate),
                        purchaseLoeCreatedAt(endDate)
                )
                .orderBy(getOrderSpecifiers(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(purchase.count())
                .from(purchase)
                .where(
                        purchaseByUserId(userId),
                        purchaseByStatus(status),
                        purchaseGoeCreatedAt(startDate),
                        purchaseLoeCreatedAt(endDate)
                )
                .fetchOne();

        return new PageImpl<>(contente, pageable, total);
    }

    private BooleanExpression purchaseByUserId(Long userId){
        return userId != null ? purchase.user.id.eq(userId) : null;
    }

    private BooleanExpression purchaseByStatus(PurchaseStatus status){
        return status != null ? purchase.status.eq(status) : null;
    }

    private BooleanExpression purchaseGoeCreatedAt(LocalDateTime time){
        return time != null ? purchase.createdAt.goe(time) : null;
    }

    private BooleanExpression purchaseLoeCreatedAt(LocalDateTime time){
        return time != null ? purchase.createdAt.loe(time) : null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Sort sort) {
        return sort.stream()
                .map(order -> {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;

                    switch (order.getProperty()) {
                        case "createdAt":
                            return new OrderSpecifier<>(direction, purchase.createdAt);
                        case "totalPrice":
                            return new OrderSpecifier<>(direction, purchase.totalPrice);
                        case "status":
                            return new OrderSpecifier<>(direction, purchase.status);
                        default:
                            return new OrderSpecifier<>(Order.DESC, purchase.createdAt);
                    }
                })
                .toArray(OrderSpecifier[]::new);
    }
}
