CREATE TABLE IF NOT EXISTS shipment_aud
(
    rev                     BIGINT       NOT NULL,
    revtype                 TINYINT      NOT NULL,
    shipment_id             BIGINT       NOT NULL,
    created_at              datetime(6)  NOT NULL,
    destination             varchar(255) NOT NULL,
    first_name              varchar(255) NOT NULL,
    last_name               varchar(255) NOT NULL,
    locked                  bit          NOT NULL,
    shipment_related_id     bigint       NULL,
    parcel_size             varchar(20)  NOT NULL,
    type                    varchar(7)   NOT NULL,
    recipient_city          varchar(255) NOT NULL,
    recipient_email         varchar(255) NOT NULL,
    recipient_first_name    varchar(255) NOT NULL,
    recipient_last_name     varchar(255) NOT NULL,
    recipient_postal_code   varchar(255) NOT NULL,
    recipient_street        varchar(255) NOT NULL,
    recipient_telephone     varchar(255) NOT NULL,
    sender_city             varchar(255) NOT NULL,
    sender_email            varchar(255) NOT NULL,
    sender_postal_code      varchar(255) NOT NULL,
    sender_street           varchar(255) NOT NULL,
    sender_telephone        varchar(255) NOT NULL,
    status                  varchar(10)  NOT NULL,
    updated_at              datetime(6)  NOT NULL,
    price_amount            decimal(19,4) NULL,
    price_currency          varchar(10)  NULL,
    origin_country          varchar(50)  NOT NULL,
    destination_country     varchar(50)  NOT NULL,
    shipment_priority       varchar(50)  NOT NULL,
    dangerous_good_id       bigint       NULL,
    PRIMARY KEY (shipment_id, rev)
);

CREATE TABLE if not exists dangerous_good_aud
(
    rev                     BIGINT        NOT NULL,
    revtype                 TINYINT       NOT NULL,
    dangerous_good_id       BIGINT        NOT NULL,
    shipment_id             BIGINT        NOT NULL,
    name                    varchar(255)  NOT NULL,
    description             text          NOT NULL,
    classification_code     varchar(100)  NOT NULL,
    hazard_symbols          text          NOT NULL,
    storage_requirements    varchar(100)  NOT NULL,
    handling_instructions   text          NOT NULL,
    weight                  decimal(10,2) NOT NULL,
    unit                    varchar(50)   NOT NULL,
    packaging               varchar(100)  NOT NULL,
    flammable               tinyint(1)    NOT NULL,
    corrosive               tinyint(1)    NOT NULL,
    toxic                   tinyint(1)    NOT NULL,
    emergency_contact       varchar(255)  NOT NULL,
    country_origin          varchar(100)  NOT NULL,
    safety_data_sheet       text          NOT NULL,
    PRIMARY KEY (dangerous_good_id, rev)
);

CREATE TABLE IF NOT EXISTS shipment_aud
  (
      rev                     BIGINT       NOT NULL,
      revtype                 TINYINT      NOT NULL,
      shipment_id             BIGINT       NOT NULL,
      created_at              datetime(6)  NOT NULL,
      destination             varchar(255) NOT NULL,
      first_name              varchar(255) NOT NULL,
      last_name               varchar(255) NOT NULL,
      locked                  bit          NOT NULL,
      shipment_related_id     bigint       NULL,
      parcel_size             varchar(20)  NOT NULL,
      type                    varchar(7)   NOT NULL,
      recipient_city          varchar(255) NOT NULL,
      recipient_email         varchar(255) NOT NULL,
      recipient_first_name    varchar(255) NOT NULL,
      recipient_last_name     varchar(255) NOT NULL,
      recipient_postal_code   varchar(255) NOT NULL,
      recipient_street        varchar(255) NOT NULL,
      recipient_telephone     varchar(255) NOT NULL,
      sender_city             varchar(255) NOT NULL,
      sender_email            varchar(255) NOT NULL,
      sender_postal_code      varchar(255) NOT NULL,
      sender_street           varchar(255) NOT NULL,
      sender_telephone        varchar(255) NOT NULL,
      status                  varchar(10)  NOT NULL,
      updated_at              datetime(6)  NOT NULL,
      price_amount            decimal(19,4) NULL,
      price_currency          varchar(10)  NULL,
      origin_country          varchar(50)  NOT NULL,
      destination_country     varchar(50)  NOT NULL,
      shipment_priority       varchar(50)  NOT NULL,
      dangerous_good_id       bigint       NULL,
      PRIMARY KEY (shipment_id, rev)
  );

CREATE TABLE IF NOT EXISTS dangerous_good_aud
(
    rev                     BIGINT        NOT NULL,
    revtype                 TINYINT       NOT NULL,
    dangerous_good_id       BIGINT        NOT NULL,
    shipment_id             BIGINT        NOT NULL,
    name                    varchar(255)  NOT NULL,
    description             text          NOT NULL,
    classification_code     varchar(100)  NOT NULL,
    hazard_symbols          text          NOT NULL,
    storage_requirements    varchar(100)  NOT NULL,
    handling_instructions   text          NOT NULL,
    weight                  decimal(10,2) NOT NULL,
    unit                    varchar(50)   NOT NULL,
    packaging               varchar(100)  NOT NULL,
    flammable               tinyint(1)    NOT NULL,
    corrosive               tinyint(1)    NOT NULL,
    toxic                   tinyint(1)    NOT NULL,
    emergency_contact       varchar(255)  NOT NULL,
    country_origin          varchar(100)  NOT NULL,
    safety_data_sheet       text          NOT NULL,
    PRIMARY KEY (dangerous_good_id, rev)
);

CREATE TABLE IF NOT EXISTS signature_aud
(
    rev                   BIGINT       NOT NULL,
    revtype               TINYINT      NOT NULL,
    shipment_id           BIGINT       NOT NULL,
    signer_name           varchar(30)  NOT NULL,
    signed_at             datetime     NOT NULL,
    signature_method      varchar(20)  NOT NULL,
    document_reference    varchar(20)  NOT NULL,
    signature             blob         NULL,
    PRIMARY KEY (shipment_id, rev)
);


