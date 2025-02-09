CREATE TABLE device_settings
(
    device_settings_id        VARCHAR(255) NOT NULL PRIMARY KEY,
    device_id                 BIGINT NOT NULL UNIQUE,
    cross_courier_delivery    BOOLEAN      NOT NULL,
    validate_responsible_user BOOLEAN      NOT NULL,
    validate_department_code  BOOLEAN      NOT NULL
);