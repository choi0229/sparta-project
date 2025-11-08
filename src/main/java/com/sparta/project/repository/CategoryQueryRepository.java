package com.sparta.project.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.project.entity.Category;
import org.springframework.stereotype.Repository;

import static com.sparta.project.entity.QCategory.category;

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
}
