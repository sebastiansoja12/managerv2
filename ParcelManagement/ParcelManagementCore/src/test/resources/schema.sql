CREATE TABLE IF NOT EXISTS USERS
(
    id         int          NOT NULL,
    email      varchar(255) NOT NULL,
    first_name varchar(255) DEFAULT NULL,
    last_name  varchar(255) DEFAULT NULL,
    password   varchar(255) NOT NULL,
    role       varchar(255) DEFAULT NULL,
    username   varchar(255) NOT NULL,
    depot_id   bigint       DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS PAYMENT
(
    id            bigint NOT NULL,
    amount        double NOT NULL,
    parcel_status int          DEFAULT NULL,
    payment_pass  int          DEFAULT NULL,
    payment_url   varchar(255) DEFAULT NULL,
    paypal_id     varchar(255) DEFAULT NULL,
    parcel_id     varchar(255) DEFAULT NULL
);
CREATE TABLE IF NOT EXISTS REROUTE_TOKEN
(
    id           bigint NOT NULL,
    created_date datetime     DEFAULT NULL,
    timeout      datetime     DEFAULT NULL,
    token        bigint       DEFAULT NULL,
    parcel_id    bigint       DEFAULT NULL,
    email        varchar(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS REDIRECT_TOKEN
(
    id           bigint NOT NULL,
    created_date datetime     DEFAULT NULL,
    timeout      datetime     DEFAULT NULL,
    token        bigint       DEFAULT NULL,
    parcel_id    bigint       DEFAULT NULL,
    email        varchar(255) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS PARCEL
(
    id                    bigint NOT NULL,
    first_name            varchar(255) DEFAULT NULL,
    last_name             varchar(255) DEFAULT NULL,
    sender_email          varchar(255) DEFAULT NULL,
    sender_city           varchar(255) DEFAULT NULL,
    sender_street         varchar(255) DEFAULT NULL,
    sender_postal_code    varchar(255) DEFAULT NULL,
    recipient_city        varchar(255) DEFAULT NULL,
    recipient_email       varchar(255) DEFAULT NULL,
    recipient_first_name  varchar(255) DEFAULT NULL,
    recipient_last_name   varchar(255) DEFAULT NULL,
    recipient_postal_code varchar(255) DEFAULT NULL,
    recipient_street      varchar(255) DEFAULT NULL,
    recipient_telephone   varchar(255) DEFAULT NULL,
    parcel_size           varchar(255) DEFAULT NULL,
    primary key (id)
);
CREATE TABLE IF NOT EXISTS DEPOT
(
    depot_code varchar(255) default null,
    street     varchar(255) default null,
    country    varchar(255) default null,
    city       varchar(255) default null,
    primary key (depot_code)
);
CREATE TABLE IF NOT EXISTS returning
(
    id            BIGINT PRIMARY KEY,
    parcel_id     BIGINT       NOT NULL UNIQUE,
    return_status VARCHAR(255) NOT NULL,
    return_token  VARCHAR(255) NOT NULL,
    username      VARCHAR(255) NOT NULL,
    supplier_code VARCHAR(255) NOT NULL,
    depot_code    VARCHAR(255) NOT NULL,
    FOREIGN KEY (depot_code) REFERENCES DEPOT (depot_code)
);