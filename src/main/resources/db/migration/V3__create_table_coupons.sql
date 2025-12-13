CREATE TABLE coupons (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '쿠폰 ID',
    coupon_name         VARCHAR(100) NOT NULL COMMENT '쿠폰명',
    discount_type       VARCHAR(20) NOT NULL COMMENT '할인 타입 (PERCENTAGE, FIXED)',
    discount_value      DECIMAL(10, 2) NOT NULL COMMENT '할인 값 (정률: %, 정액: 원)',
    min_order_amount    DECIMAL(10, 2) NULL COMMENT '최소 주문 금액',
    max_discount_amount DECIMAL(10, 2) NULL COMMENT '최대 할인 금액 (정률 할인 시)',
    start_date          DATETIME NOT NULL COMMENT '할인 시작일',
    end_date            DATETIME NOT NULL COMMENT '할인 종료일',
    usage_limit         INTEGER NOT NULL COMMENT '발급 한도',
    issue_count         INTEGER NOT NULL DEFAULT 0 COMMENT '발급된 개수',
    used_count          INTEGER NOT NULL DEFAULT 0 COMMENT '사용된 개수',
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE COMMENT '삭제 여부',
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',

    INDEX idx_active_coupons (is_deleted, start_date, end_date)
);