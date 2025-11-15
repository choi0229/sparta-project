package com.sparta.week3.service;

import com.sparta.week3.common.ServiceException;
import com.sparta.week3.common.ServiceExceptionCode;
import com.sparta.week3.controller.dto.AddCartItemRequest;
import com.sparta.week3.controller.dto.UpdateCartItemRequest;
import com.sparta.week3.entity.Product;
import com.sparta.week3.repository.ProductRepository;
import com.sparta.week3.repository.UserRepository;
import com.sparta.week3.service.dto.CartItemDto;
import com.sparta.week3.service.dto.CartSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartStorage cartStorage;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    private final Integer maxItems = 10;

    private final Map<String, BigDecimal> couponCodes = Map.of(
            "DOUBLE", BigDecimal.valueOf(2),
            "TRIPLE", BigDecimal.valueOf(3),
            "WELCOME10", BigDecimal.valueOf(1.1)  // 10% 추가 적립
    );

    @Transactional
    public void addItem(Long userId, AddCartItemRequest request) {
        validateUserById(userId);
        if(cartStorage.getItemCount(userId) >= maxItems){
            throw new ServiceException(ServiceExceptionCode.CART_MAX_ITEMS_EXCEEDED);
        }
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
        // 재고 체크
        if(product.getStock() < request.getQuantity()){
            throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_PRODUCT_STOCK);
        }

        CartItemDto item = CartItemDto.builder()
                .productId(product.getId())
                .productName(product.getName())
                .quantity(request.getQuantity())
                .category(product.getCategory())
                .price(product.getPrice())
                .build();
        cartStorage.addItem(userId, item);
    }

    @Transactional
    public void updateItem(Long userId, UpdateCartItemRequest request){
        validateUserById(userId);

        Product product = productRepository.findById(request.getProductId())
                        .orElseThrow(() -> new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT));
        // 재고 감소는 구매할때
        if(product.getStock() < request.getQuantity()){
            throw new ServiceException(ServiceExceptionCode.INSUFFICIENT_PRODUCT_STOCK);
        }

        cartStorage.updateItem(userId, request.getProductId(), request.getQuantity());
    }

    @Transactional
    public void deleteItem(Long userId, Long productId){
        validateUserById(userId);
        validateProductByProductId(productId);
        cartStorage.removeItem(userId, productId);
    }

    public CartItemDto getItem(Long userId, Long productId){
        validateUserById(userId);
        validateProductByProductId(productId);
        return cartStorage.getItem(userId, productId);
    }

    public CartSummaryDto getCartSummary(Long userId, String couponCode){
        validateUserById(userId);
        List<CartItemDto> items = cartStorage.getCartItems(userId);

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer itemCount = items.size();

        BigDecimal expectedPoint = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                        .multiply(item.getCategory().getPointWeight()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal multiplier = (couponCode != null && !couponCode.isEmpty())
                ? couponCodes.getOrDefault(couponCode, BigDecimal.ONE)
                : BigDecimal.ONE;

        expectedPoint = expectedPoint.multiply(multiplier).setScale(0, RoundingMode.DOWN);

        return CartSummaryDto.builder()
                .userId(userId)
                .items(items)
                .totalAmount(totalAmount)
                .totalItemCount(itemCount)
                .expectedPoints(expectedPoint)
                .build();
    }

    private void validateProductByProductId(Long productId) {
        if(!productRepository.existsById(productId)){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_PRODUCT);
        }
    }

    private void validateUserById(Long userId) {
        if(!userRepository.existsById(userId)){
            throw new ServiceException(ServiceExceptionCode.NOT_FOUND_USER);
        }
    }
}
