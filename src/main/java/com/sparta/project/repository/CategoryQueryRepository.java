package com.sparta.project.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.project.PurchaseStatus;
import com.sparta.project.entity.Category;
import com.sparta.project.entity.Product;
import com.sparta.project.repository.projection.CategorySalesProjection;
import com.sparta.project.repository.projection.ProductSalesProjection;
import org.springframework.expression.spel.ast.Projection;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.sparta.project.entity.QCategory.category;
import static com.sparta.project.entity.QProduct.product;
import static com.sparta.project.entity.QPurchase.purchase;
import static com.sparta.project.entity.QPurchaseProduct.purchaseProduct;

@Repository
public class CategoryQueryRepository {
    private final JPAQueryFactory queryFactory;

    public CategoryQueryRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Category findById(Long id) {
        return queryFactory.selectFrom(category)
                .where(category.id.eq(id))
                .fetchOne();
    }

    public boolean hasChild(Long parentId){
        Category child = queryFactory.selectFrom(category)
                .where(parentId != null ? category.parent.id.eq(parentId) : null)
                .fetchFirst();

        return child != null;
    }

    public boolean hasProduct(Long categoryId){
        Product relProduct = queryFactory.selectFrom(product)
                .where(categoryId != null ? product.category.id.eq(categoryId) : null)
                .fetchFirst();
        return relProduct != null;
    }

    public List<Category> findRootCategories() {
        return queryFactory.selectFrom(category)
                .where(category.parent.isNull())
                .orderBy(category.createdAt.asc())
                .fetch();
    }

    public List<ProductSalesProjection> getTopProducts(Long categoryId, int limit) {
        return queryFactory
                .select(Projections.constructor(
                        ProductSalesProjection.class,
                        product.id,
                        product.name,
                        product.category.id,
                        purchaseProduct.quantity.sum(),
                        purchaseProduct.quantity.multiply(product.price).sum()
                ))
                .from(product)
                .leftJoin(purchaseProduct).on(purchaseProduct.product.id.eq(product.id))
                .leftJoin(purchase).on(purchaseProduct.purchase.id.eq(purchase.id)
                        .and(purchase.status.eq(PurchaseStatus.COMPLETED)))
                .where(product.category.id.eq(categoryId))
                .groupBy(product.id)
                .orderBy(purchaseProduct.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }

    public List<CategorySalesProjection> getTopCategories(int limit){
        return queryFactory
                .select(Projections.constructor(
                        CategorySalesProjection.class,
                        category.id,
                        category.name,
                        product.count(),
                        purchaseProduct.quantity.sum(),
                        purchaseProduct.quantity.multiply(product.price).sum()
                ))
                .from(category)
                .leftJoin(category.products, product)
                .leftJoin(purchaseProduct).on(purchaseProduct.product.id.eq(product.id))
                .leftJoin(purchase).on(purchaseProduct.purchase.id.eq(purchase.id)
                        .and(purchase.status.eq(PurchaseStatus.COMPLETED)))
                .groupBy(category.id)
                .orderBy(purchaseProduct.quantity.sum().desc())
                .limit(limit)
                .fetch();
    }
}
