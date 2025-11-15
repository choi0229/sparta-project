CREATE TABLE point_wallet
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    balance DECIMAL(10, 2),
    active  BOOLEAN DEFAULT TRUE,
    created_at  DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    updated_at  DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
);

CREATE INDEX idx_point_wallet_user_id ON point_wallet (user_id);