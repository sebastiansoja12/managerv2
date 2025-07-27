ALTER TABLE shipment
    ADD COLUMN origin_country VARCHAR(50) NOT NULL;

ALTER TABLE shipment
    ADD COLUMN destination_country VARCHAR(50) NOT NULL;
