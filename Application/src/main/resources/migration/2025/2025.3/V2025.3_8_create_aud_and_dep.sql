CREATE TABLE department_aud
(
    rev                 BIGINT NOT NULL,
    revtype             TINYINT NOT NULL,
    department_code     VARCHAR(255) NOT NULL,
    city                VARCHAR(255) NOT NULL,
    street              VARCHAR(255) NOT NULL,
    tax_id              VARCHAR(255) NOT NULL,
    opening_hours       VARCHAR(255) NOT NULL,
    postal_code         VARCHAR(255) NOT NULL,
    telephone_number    VARCHAR(255) NOT NULL,
    active              TINYINT(1) DEFAULT 1 NULL,
    country_code        VARCHAR(4) NOT NULL,
    department_type     VARCHAR(50) DEFAULT 'BRANCH' NOT NULL,
    created_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at          TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    email               VARCHAR(30) NOT NULL,
    status              VARCHAR(10) DEFAULT 'ACTIVE' NULL,
    admin_user_id       BIGINT NULL,
    created_by          BIGINT NOT NULL,
    last_modified_by    BIGINT NULL,
    PRIMARY KEY (department_code, rev)
);

CREATE TABLE revinfo
(
    rev BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    revtstmp BIGINT NOT NULL
);
