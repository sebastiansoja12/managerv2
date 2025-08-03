CREATE TABLE signature
(
    signer_name        VARCHAR(30) NOT NULL,
    signed_at          DATETIME     NOT NULL,
    signature_method   VARCHAR(20) NOT NULL,
    document_reference VARCHAR(20) NOT NULL,
    shipment_id        BIGINT NOT NULL,
    signature          BLOB,
    PRIMARY KEY (shipment_id)
);
