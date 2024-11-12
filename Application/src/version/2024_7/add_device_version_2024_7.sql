CREATE TABLE IF NOT EXISTS device_version
(
    id          BIGINT       NOT NULL PRIMARY KEY,
    device_type VARCHAR(10)  NOT NULL,
    version     VARCHAR(10) NOT NULL,
    device_id   VARCHAR(30) NOT NULL,
    last_update TIMESTAMP    NOT NULL
);
