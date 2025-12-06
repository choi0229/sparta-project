package com.sparta.msa.project_part_3.domain.cartItem.controller;

import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemRequest;
import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cartItem.service.CartItemService;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import com.sparta.msa.project_part_3.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping("/add")
    public ApiResponse<CartItemResponse> addItem(@Valid @RequestBody CartItemRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {

        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        Long userId = userDetails.getUserId();

        CartItemResponse response = cartItemService.addItem(request, userId);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getAllItems(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        Long userId = userDetails.getUserId();

        List<CartItemResponse> response = cartItemService.getAllItems(userId);
        return ApiResponse.ok(response);
    }

    @PutMapping("/update/quantity")
    public ApiResponse<CartItemResponse> updateQuantity(@AuthenticationPrincipal CustomUserDetails userDetails
    ,@Valid @RequestBody CartItemRequest request){
        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        Long userId = userDetails.getUserId();
        CartItemResponse response = cartItemService.updateQuantity(userId, request);
        return ApiResponse.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteItem(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long productId) {
        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        Long userId = userDetails.getUserId();
        cartItemService.deleteCartItem(userId, productId);
        return ApiResponse.ok();
    }
}
