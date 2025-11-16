package com.sparta.week3.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ServiceExceptionCode {
    INTERNAL_ERROR("서버에러"),
    // 서비스 로직에서 필요한 Exception들을 Enum
    NOT_FOUND_PRODUCT("상품을 찾을수 없습니다"),
    NOT_FOUND_USER("사용자를 찾을 수 없습니다"),
    ALREADY_EXISTS_USER("동일한 이메일의 사용자가 이미 존재합니다"),
    INSUFFICIENT_PRODUCT_STOCK("상품 재고가 부족합니다"),
    INVALID_ORDER_QUANTITY("주문 수량이 올바르지 않습니다"),
    NOT_FOUND_ORDER("주문을 찾을 수 없습니다"),
    NOT_FOUND_CATEGORY("카테고리를 찾을 수 없습니다"),
    ALREADY_EXISTS_PRODUCT("동일한 이름의 상품이 이미 존재합니다"),
    INVALID_PRODUCT_STOCK("상품 재고가 올바르지 않습니다"),
    INVALID_PRODUCT_PRICE("상품 가격이 올바르지 않습니다"),
    OUT_OF_STOCK_PRODUCT("재고가 없습니다."),
    NOT_FOUND_PURCHASE("구매 내역을 찾을 수 없습니다"),
    REFUND_NOT_ALLOWED("환불을 진행할 수 없는 상태입니다"),
    NOT_FOUND_PRODUCT_FROM_CART("장바구니에 해당 상품이 없습니다"),
    CART_MAX_ITEMS_EXCEEDED("장바구니 최대 품목 수를 초과했습니다"),
    POINT_WALLET_NOT_ALLOWED("포인트를 이용할 수 없는 상태입니다"),
    INSUFFICIENT_POINT_BALANCE("포인트 잔액이 부족합니다");

    final String message;
}

