CREATE TABLE IF NOT EXISTS USERS
(
    id         BIGINT       NOT NULL,
    email      VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) DEFAULT NULL,
    last_name  VARCHAR(255) DEFAULT NULL,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) DEFAULT NULL,
    username   VARCHAR(255) NOT NULL,
    depot_code VARCHAR(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS SUPPLIER
(
    supplier_code VARCHAR(255) NOT NULL,
    first_name    VARCHAR(255) DEFAULT NULL,
    last_name     VARCHAR(255) DEFAULT NULL,
    username      VARCHAR(255) NOT NULL,
    depot_code    VARCHAR(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS PAYMENT
(
    id            BIGINT NOT NULL,
    amount        DOUBLE NOT NULL,
    parcel_status INT          DEFAULT NULL,
    payment_pass  INT          DEFAULT NULL,
    payment_url   VARCHAR(255) DEFAULT NULL,
    paypal_id     VARCHAR(255) DEFAULT NULL,
    parcel_id     VARCHAR(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS REROUTE_TOKEN
(
    id           BIGINT NOT NULL,
    created_date DATETIME DEFAULT NULL,
    timeout      DATETIME DEFAULT NULL,
    token        BIGINT   DEFAULT NULL,
    parcel_id    BIGINT   DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS PARCEL
(
    id                    BIGINT NOT NULL,
    first_name            VARCHAR(255) NOT NULL,
    last_name             VARCHAR(255) NOT NULL,
    sender_email          VARCHAR(255) NOT NULL,
    sender_city           VARCHAR(255) NOT NULL,
    sender_street         VARCHAR(255) NOT NULL,
    sender_postal_code    VARCHAR(255) NOT NULL,
    recipient_city        VARCHAR(255) NOT NULL,
    recipient_email       VARCHAR(255) NOT NULL,
    recipient_first_name  VARCHAR(255) NOT NULL,
    recipient_last_name   VARCHAR(255) NOT NULL,
    recipient_postal_code VARCHAR(255) NOT NULL,
    recipient_street      VARCHAR(255) NOT NULL,
    recipient_telephone   VARCHAR(255) NOT NULL,
    price                 DOUBLE NOT NULL,
    parcel_size           VARCHAR(255) NOT NULL,
    parcelStatus                INT NOT NULL,
    destination           VARCHAR(255) NOT NULL,
    parcel_related_id     BIGINT  DEFAULT NULL,
    PRIMARY KEY (id)
);

-- CREATE TABLE statement for DepotEntity
CREATE TABLE IF NOT EXISTS DEPOT (
depot_code VARCHAR(255) NOT NULL,
city VARCHAR(255) NOT NULL,
street VARCHAR(255) NOT NULL,
country VARCHAR(255) NOT NULL,
PRIMARY KEY (depot_code)
);

CREATE TABLE IF NOT EXISTS ROUTE
(
    id            VARCHAR(255) NOT NULL,
    created       DATETIME     DEFAULT NULL,
    depot_code    VARCHAR(255) DEFAULT NULL,
    parcel_id     BIGINT       DEFAULT NULL,
    supplier_code VARCHAR(255) DEFAULT NULL,
    user_id       INT          DEFAULT NULL,
    PRIMARY KEY (id)
);
