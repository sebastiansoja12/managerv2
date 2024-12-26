CREATE TABLE IF NOT EXISTS delivery_missed_details
(
    delivery_missed_detail_id INTEGER   NOT NULL,
    planned_delivery_date     TIMESTAMP NOT NULL,
    delivery_date             TIMESTAMP,
    created                   TIMESTAMP NOT NULL,
    new_proposed_date         TIMESTAMP,
    address_changed           BOOLEAN   NOT NULL,
    delivery_attempt_number   INT       NOT NULL,
    penalty_fee               DECIMAL(19, 4),
    suggested_action          VARCHAR(500),
    incident_report           VARCHAR(2000),
    shipment_id               BIGINT,
    PRIMARY KEY (delivery_missed_detail_id)
);
