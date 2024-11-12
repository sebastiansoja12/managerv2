CREATE TABLE IF NOT EXISTS device
(
    device_id   BIGINT       NOT NULL PRIMARY KEY,
    version     VARCHAR(10) NOT NULL,
    user_id     BIGINT       NOT NULL,
    depot_code  VARCHAR(3) NOT NULL,
    device_type VARCHAR(10)  NOT NULL,
    active      BOOLEAN      NOT NULL
);



CREATE TABLE IF NOT EXISTS device_pair
(
    device_pair_id    BIGINT  NOT NULL PRIMARY KEY,
    device_entity_id  BIGINT,
    paired            BOOLEAN NOT NULL,
    pair_time         TIMESTAMP,
    error_description VARCHAR(255),
    FOREIGN KEY (device_entity_id) REFERENCES device (device_id) ON DELETE SET NULL
);
