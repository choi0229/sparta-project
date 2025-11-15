package com.sparta.week3.service;

import com.sparta.week3.service.dto.CartItemDto;

import java.util.List;

public interface CartStorage {
// TODO 나중에 Redis로 전환
    void addItem(Long userId, CartItemDto item);

    void updateItem(Long userId, Long productId, Integer quantity);

    void removeItem(Long userId, Long productId);

    List<CartItemDto> getCartItems(Long userId);

    CartItemDto getItem(Long userId, Long productId);

    void clearCart(Long userId);

    int getItemCount(Long userId);
}
