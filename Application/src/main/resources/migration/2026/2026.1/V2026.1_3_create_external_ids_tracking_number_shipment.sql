ALTER TABLE shipment
    ADD COLUMN external_id varchar(40) NOT NULL,
ADD COLUMN tracking_number VARCHAR(255) NOT NULL;


ALTER TABLE shipment_aud
    ADD COLUMN external_id varchar(40) NOT NULL,
ADD COLUMN tracking_number VARCHAR(255) NOT NULL;


CREATE TABLE tracking_sequence
(
    id         varchar(10) not null,
    version    bigint      not null,
    next_value bigint      not null
);

