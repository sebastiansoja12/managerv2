CREATE TABLE dangerous_good
(
    shipment_id           BIGINT         NOT NULL,
    name                  VARCHAR(255)   NOT NULL,
    description           VARCHAR(255)   NOT NULL,
    classification_code   VARCHAR(255)   NOT NULL,
    hazard_symbols        VARCHAR(255)   NOT NULL,
    storage_requirements  VARCHAR(255)   NOT NULL,
    handling_instructions VARCHAR(255)   NOT NULL,
    weight                DECIMAL(10, 2) NOT NULL,
    unit                  VARCHAR(15)    NOT NULL,
    packaging             VARCHAR(255)   NOT NULL,
    flammable             BOOLEAN        NOT NULL,
    corosive              BOOLEAN        NOT NULL,
    toxic                 BOOLEAN        NOT NULL,
    emergency_contact     VARCHAR(255)   NOT NULL,
    country_origin        VARCHAR(255)   NOT NULL,
    safety_data_sheet     VARCHAR(255)   NOT NULL,
    PRIMARY KEY (shipment_id)
);
