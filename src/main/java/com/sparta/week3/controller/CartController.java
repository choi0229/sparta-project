package com.sparta.week3.controller;

import com.sparta.week3.common.ApiResponse;
import com.sparta.week3.common.ServiceException;
import com.sparta.week3.controller.dto.AddCartItemRequest;
import com.sparta.week3.controller.dto.UpdateCartItemRequest;
import com.sparta.week3.service.CartService;
import com.sparta.week3.service.dto.CartItemDto;
import com.sparta.week3.service.dto.CartSummaryDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items/{userId}")
    public ResponseEntity<ApiResponse<Void>> addItem(@PathVariable Long userId, @Valid @RequestBody AddCartItemRequest request){
        try{
            cartService.addItem(userId, request);
            return ResponseEntity.ok(ApiResponse.success(null));
        }catch (ServiceException e){
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.error(e.getMessage(), e.getMessage()));
        }
    }

    @PutMapping("/items/{userId}")
    public ResponseEntity<ApiResponse<Void>> updateItem(@PathVariable Long userId, @Valid @RequestBody UpdateCartItemRequest request){
        cartService.updateItem(userId, request);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @DeleteMapping("/items/{userId}/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable Long userId, @PathVariable Long productId){
        cartService.deleteItem(userId, productId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/items/{userId}/{productId}")
    public ResponseEntity<ApiResponse<CartItemDto>> getItem(@PathVariable Long userId, @PathVariable Long productId){
        CartItemDto response = cartService.getItem(userId, productId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/items/summary/{userId}/{couponCode}")
    public ResponseEntity<ApiResponse<CartSummaryDto>> getCartSummary(
            @PathVariable Long userId,
            @PathVariable(required = false) String couponCode
    ){
        CartSummaryDto response = cartService.getCartSummary(userId, couponCode);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
