package com.sparta.msa.project_part_3.domain.cartItem.controller;

import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemRequest;
import com.sparta.msa.project_part_3.domain.cartItem.dto.CartItemResponse;
import com.sparta.msa.project_part_3.domain.cartItem.service.CartItemService;
import com.sparta.msa.project_part_3.global.exception.DomainException;
import com.sparta.msa.project_part_3.global.exception.DomainExceptionCode;
import com.sparta.msa.project_part_3.global.response.ApiResponse;
import com.sparta.msa.project_part_3.global.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
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
    public ApiResponse<CartItemResponse> addItem(
            @Valid @RequestBody CartItemRequest request
            , @AuthenticationPrincipal CustomUserDetails userDetails
            , HttpSession session
    ) {
        String email = userDetails != null ? userDetails.getUsername() : null;

        CartItemResponse response = cartItemService.addItem(request, email, session);
        return ApiResponse.ok(response);
    }

    @GetMapping
    public ApiResponse<List<CartItemResponse>> getAllItems(@AuthenticationPrincipal CustomUserDetails userDetails ,HttpSession session) {
        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        String email = userDetails.getUsername();

        List<CartItemResponse> response = cartItemService.getAllItems(email, session);
        return ApiResponse.ok(response);
    }

    @PutMapping("/update/quantity")
    public ApiResponse<CartItemResponse> updateQuantity(@AuthenticationPrincipal CustomUserDetails userDetails
    ,@Valid @RequestBody CartItemRequest request){
        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        String email = userDetails.getUsername();
        CartItemResponse response = cartItemService.updateQuantity(email, request);
        return ApiResponse.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ApiResponse<Void> deleteItem(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long productId) {
        if(userDetails == null){
            throw new DomainException(DomainExceptionCode.INVALID_LOGIN);
        }

        String email = userDetails.getUsername();
        cartItemService.deleteCartItem(email, productId);
        return ApiResponse.ok();
    }
}
