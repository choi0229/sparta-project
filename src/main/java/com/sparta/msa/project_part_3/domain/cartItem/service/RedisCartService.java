package com.sparta.msa.project_part_3.domain.cartItem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemRequest;
import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cartItem.dto.GuestCartItem;
import com.sparta.msa.project_part_3.domain.cartItem.entity.CartItem;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import com.sparta.msa.project_part_3.global.util.JsonUtil;
import io.swagger.v3.core.util.Json;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisCartService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ProductRepository productRepository;

    private static final String GUEST_CART_KEY = "guest:cart";

    public CartItemResponse addGuestCartItem(String sessionId, CartItemRequest request) {
        String redisKey = GUEST_CART_KEY + sessionId;
        String productKey = request.productId().toString();

        Product product = productRepository.findById(request.productId())
                .orElseThrow(()-> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));

        String existingJson = (String) redisTemplate.opsForHash().get(redisKey, productKey);

        GuestCartItem newItem;
        if(existingJson != null) {
            GuestCartItem existingItem = JsonUtil.fromJson(existingJson, GuestCartItem.class);
            int totalQuantity = existingItem.quantity() + request.quantity();

            if(totalQuantity > product.getStock()){
                throw new DomainException(DomainExceptionCode.INSUFFICIENT_STOCK);
            }

            newItem = GuestCartItem.of(product, totalQuantity);
        }else{
            if(request.quantity() > product.getStock()){
                throw new DomainException(DomainExceptionCode.INSUFFICIENT_STOCK);
            }

            newItem = GuestCartItem.of(product, request.quantity());
        }

        redisTemplate.opsForHash().put(redisKey, productKey, JsonUtil.toJson(newItem));

        return newItem.toCartItemResponse();
    }

    public List<CartItemResponse> getCGuestCartItems(String sessionId) {
        String redisKey = GUEST_CART_KEY + sessionId;

        Map<Object, Object> entries = redisTemplate.opsForHash().entries(redisKey);

        return entries.values().stream()
                .map(json -> JsonUtil.fromJson((String) json, GuestCartItem.class))
                .map(GuestCartItem::toCartItemResponse)
                .toList();
    }
}
