CREATE TABLE point_transaction
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    point_wallet_id  BIGINT NOT NULL,
    purchase_id BIGINT NOT NULL,
    type        VARCHAR(20) NOT NULL COMMENT 'EARN, DEDUCTED, EXTINCTION',
    amount      DECIMAL(10, 2) NOT NULL,
    expires_at   DATETIME(6),
    created_at      DATETIME(6)    DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE INDEX idx_point_wallet_id ON point_transaction(point_wallet_id);
CREATE INDEX idx_purchase_id ON point_transaction(purchase_id);