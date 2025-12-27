ALTER TABLE shipment
    ADD COLUMN external_route_id VARCHAR(255),
    ADD COLUMN external_return_id BIGINT;

ALTER TABLE shipment_aud
    ADD COLUMN external_route_id VARCHAR(255),
    ADD COLUMN external_return_id BIGINT;
