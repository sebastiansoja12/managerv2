CREATE TABLE refresh_token
(
    id           BIGINT PRIMARY KEY,
    created_date TIMESTAMP(6) NOT NULL,
    token        VARCHAR(255) NOT NULL,
    expired      BIT(1)       NOT NULL,
    revoked      BIT(1)       NOT NULL,
    expiry_date  TIMESTAMP(6) NOT NULL,
    username     VARCHAR(255) NOT NULL
);