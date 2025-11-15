create table product
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)     NOT NULL,
    price       DECIMAL(10, 2)  NOT NULL,
    stock       INT             NOT NULL DEFAULT 0,
    category    VARCHAR(50)     NOT NULL,
    created_at  DATETIME                DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)