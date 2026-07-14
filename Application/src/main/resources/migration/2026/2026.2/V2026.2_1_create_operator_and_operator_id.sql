CREATE TABLE operators
(
    operator_id                     BIGINT       NOT NULL PRIMARY KEY,
    registering_user_id             BIGINT,
    tax_id                          VARCHAR(64)  NOT NULL,
    supports_lockers                BOOLEAN      NOT NULL DEFAULT FALSE,
    supports_international_shipping BOOLEAN      NOT NULL DEFAULT FALSE,
    supports_cash_on_delivery       BOOLEAN      NOT NULL DEFAULT FALSE,
    contact_phone                   VARCHAR(64),
    contact_email                   VARCHAR(255),
    name                            VARCHAR(255) NOT NULL,
    status                          VARCHAR(32)  NOT NULL,
    created_at                      TIMESTAMP    NOT NULL,
    updated_at                      TIMESTAMP    NOT NULL
);

CREATE TABLE operators_aud
(
    operator_id                     BIGINT NOT NULL,
    rev                             BIGINT NOT NULL,
    revtype                         TINYINT,
    registering_user_id             BIGINT,
    tax_id                          VARCHAR(64),
    supports_lockers                BOOLEAN,
    supports_international_shipping BOOLEAN,
    supports_cash_on_delivery       BOOLEAN,
    contact_phone                   VARCHAR(64),
    contact_email                   VARCHAR(255),
    name                            VARCHAR(255),
    status                          VARCHAR(32),
    created_at                      TIMESTAMP,
    updated_at                      TIMESTAMP,
    PRIMARY KEY (operator_id, rev)
);

ALTER TABLE operators_aud
    ADD CONSTRAINT fk_operators_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev);

CREATE TABLE operator_configurations
(
    operator_id                     BIGINT NOT NULL PRIMARY KEY,
    supports_domestic_shipping      BOOLEAN,
    supports_international_shipping BOOLEAN,
    supports_express_shipping       BOOLEAN,
    supports_same_day_delivery      BOOLEAN,
    supports_cash_on_delivery       BOOLEAN,
    supports_parcel_lockers         BOOLEAN,
    supports_pickup_points          BOOLEAN,
    supports_home_delivery          BOOLEAN,
    supports_saturday_delivery      BOOLEAN,
    supports_sunday_delivery        BOOLEAN,
    supports_return_shipments       BOOLEAN,
    provides_tracking               BOOLEAN,
    provides_insurance              BOOLEAN,
    max_weight                      DOUBLE,
    min_weight                      DOUBLE,
    max_length                      DOUBLE,
    max_width                       DOUBLE,
    max_height                      DOUBLE,
    max_shipment_value              DOUBLE,
    min_delivery_days               INT,
    max_delivery_days               INT,
    express_delivery_days           INT,
    same_day_delivery_hours         INT,
    international_delivery_days     INT,
    contract_start_date             DATE,
    contract_end_date               DATE,
    founded_date                    DATE
);

CREATE TABLE operator_configurations_aud
(
    operator_id                     BIGINT NOT NULL,
    rev                             BIGINT NOT NULL,
    revtype                         TINYINT,
    supports_domestic_shipping      BOOLEAN,
    supports_international_shipping BOOLEAN,
    supports_express_shipping       BOOLEAN,
    supports_same_day_delivery      BOOLEAN,
    supports_cash_on_delivery       BOOLEAN,
    supports_parcel_lockers         BOOLEAN,
    supports_pickup_points          BOOLEAN,
    supports_home_delivery          BOOLEAN,
    supports_saturday_delivery      BOOLEAN,
    supports_sunday_delivery        BOOLEAN,
    supports_return_shipments       BOOLEAN,
    provides_tracking               BOOLEAN,
    provides_insurance              BOOLEAN,
    max_weight                      DOUBLE,
    min_weight                      DOUBLE,
    max_length                      DOUBLE,
    max_width                       DOUBLE,
    max_height                      DOUBLE,
    max_shipment_value              DOUBLE,
    min_delivery_days               INT,
    max_delivery_days               INT,
    express_delivery_days           INT,
    same_day_delivery_hours         INT,
    international_delivery_days     INT,
    contract_start_date             DATE,
    contract_end_date               DATE,
    founded_date                    DATE,
    PRIMARY KEY (operator_id, rev)
);

ALTER TABLE operator_configurations_aud
    ADD CONSTRAINT fk_operator_configurations_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev);

ALTER TABLE department
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE users
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE users
    ADD COLUMN IF NOT EXISTS initial BOOLEAN NOT NULL DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS department_aud
(
    rev                 BIGINT NOT NULL,
    revtype             TINYINT NULL,
    department_code     VARCHAR(255) NOT NULL,
    city                VARCHAR(255) NULL,
    street              VARCHAR(255) NULL,
    tax_id              VARCHAR(255) NULL,
    opening_hours       VARCHAR(255) NULL,
    postal_code         VARCHAR(255) NULL,
    telephone_number    VARCHAR(255) NULL,
    country_code        VARCHAR(8) NULL,
    department_type     VARCHAR(64) NULL,
    created_at          TIMESTAMP NULL,
    updated_at          TIMESTAMP NULL,
    email               VARCHAR(255) NULL,
    status              VARCHAR(32) NULL,
    latitude            DOUBLE NULL,
    longitude           DOUBLE NULL,
    admin_user_id       BIGINT NULL,
    created_by          BIGINT NULL,
    last_modified_by    BIGINT NULL,
    PRIMARY KEY (department_code, rev)
);

ALTER TABLE department_aud
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE shipment
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE shipment_aud
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE delivery
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE suppliers
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE suppliers_aud
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE device
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE terminals
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE scanners
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE mobiles
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE process_logs
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE process_logs_rd
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE communication_log_details
    ADD COLUMN operator_id BIGINT NULL;

ALTER TABLE communication_log_details_rd
    ADD COLUMN operator_id BIGINT NULL;

CREATE INDEX idx_department_operator_id ON department (operator_id);
CREATE INDEX idx_users_operator_id ON users (operator_id);
CREATE INDEX idx_shipment_operator_id ON shipment (operator_id);
CREATE INDEX idx_suppliers_operator_id ON suppliers (operator_id);
