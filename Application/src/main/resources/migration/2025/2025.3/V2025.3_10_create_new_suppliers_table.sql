CREATE TABLE suppliers
(
    supplier_id           BIGINT       NOT NULL,
    supplier_code         VARCHAR(50),
    first_name            VARCHAR(100),
    last_name             VARCHAR(100),
    telephone_number      VARCHAR(20),
    department_code       VARCHAR(50),
    status                VARCHAR(50),
    user_status           VARCHAR(50),
    vehicle_id            BIGINT,
    device_id             BIGINT,
    certificate_number    VARCHAR(100),
    issue_date            DATETIME,
    expiry_date           DATETIME,
    authority             VARCHAR(100),
    valid                 BOOLEAN,
    driver_license_number VARCHAR(50),
    acquired_date         DATETIME,
    api_key               VARCHAR(255),
    terms_accepted        BOOLEAN,
    created_user_id       BIGINT,
    created_at            DATETIME     NOT NULL,
    updated_at            DATETIME,
    area_name             VARCHAR(100) NOT NULL,
    city                  VARCHAR(100),
    district              VARCHAR(100),
    municipality          VARCHAR(100),
    region                VARCHAR(100),
    country               VARCHAR(100),
    PRIMARY KEY (supplier_id)
);

CREATE TABLE suppliers_supported_package_types
(
    supplier_id  BIGINT      NOT NULL,
    package_type VARCHAR(50) NOT NULL,
    PRIMARY KEY (supplier_id, package_type),
    FOREIGN KEY (supplier_id) REFERENCES suppliers (supplier_id)
);

CREATE TABLE delivery_area_postal_codes
(
    delivery_area_id BIGINT      NOT NULL,
    postal_code      VARCHAR(20) NOT NULL,
    PRIMARY KEY (delivery_area_id, postal_code),
    FOREIGN KEY (delivery_area_id) REFERENCES suppliers (supplier_id)
);

INSERT INTO suppliers (supplier_id,
                      supplier_code,
                      first_name,
                      last_name,
                      telephone_number,
                      department_code,
                      status,
                      user_status,
                      vehicle_id,
                      device_id,
                      certificate_number,
                      issue_date,
                      expiry_date,
                      authority,
                      valid,
                      driver_license_number,
                      acquired_date,
                      api_key,
                      terms_accepted,
                      created_user_id,
                      created_at,
                      updated_at,
                      area_name,
                      city,
                      district,
                      municipality,
                      region,
                      country)
VALUES (1,
        'SUP123',
        'Jan',
        'Kowalski',
        '123456789',
        'DEP01',
        'ACTIVE',
        'USER_NOT_CREATED',
        1001,
        2001,
        'CERT-98765',
        '2023-01-01 00:00:00',
        '2025-01-01 00:00:00',
        'Transport Authority',
        TRUE,
        'DL-123456',
        '2020-06-15 00:00:00',
        'apikey_123456',
        TRUE,
        5001,
        NOW(),
        NOW(),
        'Downtown Area',
        'Warsaw',
        'Śródmieście',
        'Warsaw Municipality',
        'Mazowieckie',
        'Poland');

INSERT INTO suppliers_supported_package_types (supplier_id, package_type)
VALUES (1, 'SMALL'),
       (1, 'MEDIUM'),
       (1, 'LARGE');

INSERT INTO delivery_area_postal_codes (delivery_area_id, postal_code)
VALUES (1, '00-001'),
       (1, '00-002'),
       (1, '00-003');
