package com.sparta.msa.project_part_3.domain.cartItem.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.project_part_3.domain.cartItem.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sparta.msa.project_part_3.domain.cartItem.entity.QCartItem.cartItem;
import static com.sparta.msa.project_part_3.domain.product.entity.QProduct.product;
import static com.sparta.msa.project_part_3.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class CartItemQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<CartItem> findAllByUserIdWithProduct(Long userId){
        return queryFactory
                .selectFrom(cartItem)
                .join(cartItem.product, product).fetchJoin()
                .join(cartItem.user, user).fetchJoin()
                .where(cartItem.user.id.eq(userId))
                .fetch();
    }
}
