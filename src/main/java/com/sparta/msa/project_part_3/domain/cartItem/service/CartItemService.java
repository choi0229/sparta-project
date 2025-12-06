package com.sparta.msa.project_part_3.domain.cartItem.service;

import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemRequest;
import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cartItem.entity.CartItem;
import com.sparta.msa.project_part_3.domain.cartItem.repository.CartItemQueryRepository;
import com.sparta.msa.project_part_3.domain.cartItem.repository.CartItemRepository;
import com.sparta.msa.project_part_3.domain.product.entity.Product;
import com.sparta.msa.project_part_3.domain.product.repository.ProductRepository;
import com.sparta.msa.project_part_3.domain.user.entity.User;
import com.sparta.msa.project_part_3.domain.user.repository.UserRepository;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemQueryRepository cartItemQueryRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CartItemResponse addItem(CartItemRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_USER));

        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));

        if(request.quantity() > product.getStock()){
            throw new DomainException(DomainExceptionCode.INSUFFICIENT_STOCK);
        }

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, product.getId())
                .orElse(null);

        if(cartItem != null){
            int totalQuantity = cartItem.getQuantity() + request.quantity();
            if(totalQuantity > product.getStock()){
                throw new DomainException(DomainExceptionCode.INSUFFICIENT_STOCK);
            }
            cartItem.addQuantity(request.quantity());
        }else{
            if(request.quantity() > product.getStock()){
                throw new DomainException(DomainExceptionCode.INSUFFICIENT_STOCK);
            }
            cartItem = CartItem.builder()
                    .product(product)
                    .user(user)
                    .quantity(request.quantity())
                    .build();
            cartItemRepository.save(cartItem);
        }

        BigDecimal totalPrice = cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));

        return CartItemResponse.builder()
                .productName(cartItem.getProduct().getName())
                .userName(cartItem.getUser().getName())
                .quantity(cartItem.getQuantity())
                .totalPrice(totalPrice)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getAllItems(Long userId) {
        List<CartItem> cartItems = cartItemQueryRepository.findAllByUserIdWithProduct(userId);

        return cartItems.stream()
                .map(CartItemResponse::from)
                .toList();
    }

    @Transactional
    public CartItemResponse updateQuantity(Long userId, CartItemRequest request) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, request.productId())
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_CARTITEM));

        if(request.quantity() > cartItem.getProduct().getStock()){
            throw new DomainException(DomainExceptionCode.INSUFFICIENT_STOCK);
        }

        cartItem.updateQuantity(request.quantity());

        if(cartItem.getQuantity() == 0){
            cartItemRepository.delete(cartItem);
            return null;
        }
        return CartItemResponse.from(cartItem);
    }

    @Transactional
    public void deleteCartItem(Long userId, Long productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_CARTITEM));

        cartItemRepository.delete(cartItem);
    }
}
