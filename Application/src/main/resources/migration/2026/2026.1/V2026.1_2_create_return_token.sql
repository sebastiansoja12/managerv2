CREATE TABLE return_tokens
(
    return_token_id BIGINT       NOT NULL,
    shipment_id     BIGINT       NOT NULL,
    token           VARCHAR(255) NOT NULL,
    expires_at      TIMESTAMP    NOT NULL,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (return_token_id),
    UNIQUE KEY uq_return_tokens_token (token),
    KEY             idx_return_tokens_shipment_id (shipment_id),
    KEY             idx_return_tokens_expires_at (expires_at)
);
