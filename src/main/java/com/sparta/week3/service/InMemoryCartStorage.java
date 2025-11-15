package com.sparta.week3.service;

import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.entity.Product;
import com.sparta.week3.service.dto.CartItemDto;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Primary
public class InMemoryCartStorage implements CartStorage {
    private final Map<Long, Map<Long, CartItemDto>> storage = new ConcurrentHashMap<>();

    @Override
    public void addItem(Long userId, CartItemDto item){
        storage.computeIfAbsent(userId, k -> new ConcurrentHashMap<>())
                .merge(item.getProductId(), item, (existing, newItem) -> {
                    // 이미 있으면 수량만 증가
                    return CartItemDto.builder()
                            .productId(existing.getProductId())
                            .productName(existing.getProductName())
                            .quantity(existing.getQuantity() + newItem.getQuantity())
                            .price(existing.getPrice())
                            .category(existing.getCategory())
                            .build();
                });
    }

    public void updateItem(Long userId, Long productId, Integer quantity){
        Map<Long, CartItemDto> cart = storage.get(userId);
        if(cart == null || !cart.containsKey(productId)){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT_FROM_CART);
        }
        CartItemDto item = cart.get(productId);
        CartItemDto updated = CartItemDto.builder()
                .productId(productId)
                .productName(item.getProductName())
                .quantity(quantity)
                .price(item.getPrice())
                .category(item.getCategory())
                .build();
        cart.put(productId, updated);
    }

    public void removeItem(Long userId, Long productId){
        Map<Long, CartItemDto> cart = storage.get(userId);
        if(cart == null && !cart.containsKey(productId)){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT_FROM_CART);
        }
        cart.remove(productId);
    }

    public List<CartItemDto> getCartItems(Long userId){
        Map<Long, CartItemDto> cart = storage.get(userId);
        if(cart == null){
            return List.of();
        }
        return new ArrayList<>(cart.values());
    }

    public CartItemDto getItem(Long userId, Long productId){
        Map<Long, CartItemDto> cart = storage.get(userId);
        if(cart == null && !cart.containsKey(productId)){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT_FROM_CART);
        }
        return cart.get(productId);
    }

    // 초기화 같은거 누르면
    public void clearCart(Long userId){
        storage.remove(userId);
    }

    public int getItemCount(Long userId){
        Map<Long, CartItemDto> cart = storage.get(userId);
        if(cart == null){
            return 0;
        }
        return cart.size();
    }


}
