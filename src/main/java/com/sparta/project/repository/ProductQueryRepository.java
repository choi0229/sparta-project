package com.sparta.project.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.project.PurchaseStatus;
import com.sparta.project.entity.Product;
import com.sparta.project.service.dto.ProductRequestSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.sparta.project.entity.QProduct.product;
import static com.sparta.project.entity.QPurchase.purchase;
import static com.sparta.project.entity.QPurchaseProduct.purchaseProduct;

@Repository
public class ProductQueryRepository {

    private final JPAQueryFactory queryFactory;

    public ProductQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Long findIdByName(String name){
        return queryFactory.select(product.id)
                .from(product)
                .where(name != null ? product.name.eq(name) : null)
                .fetchOne();
    }

    public Product findById(Long id){
        return queryFactory.selectFrom(product)
                .where(id != null ? product.id.eq(id) : null)
                .fetchOne();
    }

    public boolean hasCompletedPurchase(Long id, PurchaseStatus status){
        Long purchaseProductId = queryFactory.select(purchaseProduct.id)
                .from(purchaseProduct)
                .join(purchaseProduct.purchase, purchase)
                .where(
                        purchaseProductIdEq(id),
                        purchaseStausEqCompleted(status)
                )
                .fetchFirst();

        return purchaseProductId != null;
    }

    private BooleanExpression purchaseStausEqCompleted(PurchaseStatus status){
        return status != null ? purchase.status.eq(status) : null;
    }

    private BooleanExpression purchaseProductIdEq(Long id){
        return id != null ? purchaseProduct.product.id.eq(id) : null;
    }

    public Page<Product> searchProducts(ProductRequestSearchDto request, Pageable pageable){
        List<Product> content = queryFactory.selectFrom(product)
                .where(
                        productCategoryIdEq(request.getCategoryId()),
                        productMinPrice(request.getMinPrice()),
                        productMaxPrice(request.getMaxPrice()),
                        productStock(request.getStockStatus())
                )
                .orderBy(getOrderSpecifiers(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long totalCount = queryFactory.select(product.count())
                .from(product)
                .where(
                        productCategoryIdEq(request.getCategoryId()),
                        productMinPrice(request.getMinPrice()),
                        productMaxPrice(request.getMaxPrice()),
                        productStock(request.getStockStatus())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, totalCount);
    }

    private BooleanExpression productCategoryIdEq(Long id){
        return id != null ? product.category.id.eq(id) : null;
    }

    private BooleanExpression productMinPrice(BigDecimal minPrice){
        return minPrice != null ? product.price.goe(minPrice) : null;
    }

    private BooleanExpression productMaxPrice(BigDecimal maxPrice){
        return maxPrice != null ? product.price.loe(maxPrice) : null;
    }

    private BooleanExpression productStock(String stockStatus){
        if(stockStatus != null){
            if(stockStatus.equals("재고 없음")){
                return product.stock.eq(0);
            }else if(stockStatus.equals("재고 부족")){
                return product.stock.goe(10);
            }
        }
        return null;
    }

    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        for (Sort.Order order : pageable.getSort()) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            if (property.equals("price")) {
                orderSpecifiers.add(new OrderSpecifier(direction, product.price));
            } else if (property.equals("createdAt")) {
                orderSpecifiers.add(new OrderSpecifier(direction, product.createdAt));
            }
        }

        return orderSpecifiers.toArray(new OrderSpecifier<?>[0]);
    }
}
