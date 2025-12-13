CREATE TABLE coupon_users(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '발행 쿠폰 ID',
    coupon_id   BIGINT NOT NULL COMMENT '쿠폰 ID',
    user_id     BIGINT COMMENT '유저 ID',
    code        VARCHAR(50) NOT NULL UNIQUE COMMENT '쿠폰 코드',
    status      VARCHAR(20) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING, ISSUED, USED, EXPIRED',
    created_at  DATETIME NOT NULL COMMENT '생성일',
    updated_at  DATETIME NOT NULL COMMENT '수정일',

    INDEX idx_coupon_user (coupon_id, user_id),
    INDEX idx_coupon_user_status (user_id, status)
)