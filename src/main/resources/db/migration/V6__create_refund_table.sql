CREATE TABLE refund
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    purchase_id BIGINT NOT NULL,
    purchase_product_id BIGINT         NOT NULL,
    amount       DECIMAL(10, 2) NOT NULL,
    quantity     INT            NOT NULL,
    reason       VARCHAR(255) NOT NULL COMMENT '환불 사유',
    status       VARCHAR(20)  NOT NULL DEFAULT 'REQUESTED' COMMENT 'REQUESTED, APPROVED, REJECTED',
    created_at   DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    processed_at        DATETIME(6)
);

CREATE INDEX idx_refund_purchase_id ON refund (purchase_id);
CREATE INDEX idx_refund_purchase_product_id ON refund (purchase_product_id);