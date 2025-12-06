CREATE TABLE cart_item
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '장바구니 ID',
    product_id  BIGINT         NOT NULL COMMENT '상품 ID',
    user_id     BIGINT         NOT NULL COMMENT '유저 ID',
    quantity    INTEGER        NOT NULL COMMENT '수량',
    created_at  DATETIME       NOT NULL COMMENT '생성 일시',
    updated_at  DATETIME       NOT NULL COMMENT '수정 일시',
    INDEX idx_cart_item_user_id (user_id),
    UNIQUE KEY uk_cart_user_product (user_id, product_id)
);