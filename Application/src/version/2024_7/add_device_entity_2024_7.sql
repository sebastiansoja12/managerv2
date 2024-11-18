CREATE TABLE IF NOT EXISTS device
(
    device_id   BIGINT       NOT NULL PRIMARY KEY,
    version     VARCHAR(10) NOT NULL,
    user_id     BIGINT       NOT NULL,
    depot_code  VARCHAR(3) NOT NULL,
    device_type VARCHAR(10)  NOT NULL,
    active      BOOLEAN      NOT NULL
);