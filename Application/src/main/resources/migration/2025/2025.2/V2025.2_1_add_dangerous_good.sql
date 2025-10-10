CREATE TABLE dangerous_good
(
    dangerous_good_id     BIGINT         NOT NULL,
    shipment_id           BIGINT         NOT NULL,
    name                  VARCHAR(255)   NOT NULL,
    description           TEXT           NOT NULL,
    classification_code   VARCHAR(100)   NOT NULL,
    hazard_symbols        TEXT           NOT NULL,
    storage_requirements  VARCHAR(100)   NOT NULL,
    handling_instructions TEXT           NOT NULL,
    weight                DECIMAL(10, 2) NOT NULL,
    unit                  VARCHAR(50)    NOT NULL,
    packaging             VARCHAR(100)   NOT NULL,
    flammable             BOOLEAN        NOT NULL,
    corrosive             BOOLEAN        NOT NULL,
    toxic                 BOOLEAN        NOT NULL,
    emergency_contact     VARCHAR(255)   NOT NULL,
    country_origin        VARCHAR(100)   NOT NULL,
    safety_data_sheet     TEXT           NOT NULL,
    PRIMARY KEY (dangerous_good_id)
);
