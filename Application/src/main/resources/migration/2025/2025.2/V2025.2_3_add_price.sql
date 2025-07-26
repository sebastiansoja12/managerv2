
CREATE TABLE price (
                       id BIGINT NOT NULL PRIMARY KEY,
                       shipment_size VARCHAR(50) NOT NULL,
                       price_amount DECIMAL(19, 2) NOT NULL,
                       price_currency_code VARCHAR(3) NOT NULL
);
