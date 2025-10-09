CREATE TABLE returning_return_package
(
    return_id           BIGINT NOT NULL,
    shipment_id         BIGINT NOT NULL,
    reason              VARCHAR(255) NOT NULL,
    return_status       VARCHAR(50)  NOT NULL,
    return_token        VARCHAR(255),
    assigned_department VARCHAR(5),
    returned_department VARCHAR(5),
    assigned_to         BIGINT,
    processed_by        BIGINT,
    reason_code         VARCHAR(20),
    created_at          TIMESTAMP    NOT NULL,
    updated_at          TIMESTAMP    NOT NULL,
    PRIMARY KEY (return_id)
);
