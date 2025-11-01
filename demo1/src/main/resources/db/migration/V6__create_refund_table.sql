CREATE TABLE refund (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        order_id BIGINT NOT NULL,
                        reason TEXT NOT NULL,
                        status VARCHAR(20) DEFAULT 'PENDING' COMMENT 'PENDING, COMPLETED, CANCELED',
                        created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
                        updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6)
)