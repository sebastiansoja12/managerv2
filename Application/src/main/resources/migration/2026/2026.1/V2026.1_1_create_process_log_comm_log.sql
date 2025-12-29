CREATE TABLE process_logs
(
    process_id        BINARY(16) NOT NULL,

    request           LONGTEXT,
    response          LONGTEXT,

    status            VARCHAR(50),
    fault_description VARCHAR(1024),

    device_id         VARCHAR(255),
    department_code   VARCHAR(255),
    user_id           VARCHAR(255),
    device_type       VARCHAR(50),
    device_user_type  VARCHAR(50),
    device_version    VARCHAR(100),

    created_at        TIMESTAMP(6),
    modified_at       TIMESTAMP(6),

    PRIMARY KEY (process_id)
);

CREATE TABLE communication_log_details
(
    id                BIGINT     NOT NULL AUTO_INCREMENT,

    process_id        BINARY(16) NOT NULL,

    device_id         VARCHAR(255),

    process_type      VARCHAR(50),
    service_type      VARCHAR(50),

    created_by        VARCHAR(255),
    updated_by        VARCHAR(255),
    department_code   VARCHAR(255),

    source_service    VARCHAR(255),
    target_service    VARCHAR(255),

    request           LONGTEXT,
    response          LONGTEXT,
    fault_description LONGTEXT,

    created_at        TIMESTAMP(6),
    updated_at        TIMESTAMP(6),

    PRIMARY KEY (id),
    KEY               idx_comm_log_process_id (process_id),

    CONSTRAINT fk_comm_log_process
        FOREIGN KEY (process_id)
            REFERENCES process_logs (process_id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
);


CREATE TABLE process_logs_rd
(
    process_id        BINARY(16) NOT NULL,

    request           LONGTEXT,
    response          LONGTEXT,

    status            VARCHAR(50),
    fault_description VARCHAR(1024),

    device_id         VARCHAR(255),
    department_code   VARCHAR(255),
    user_id           VARCHAR(255),
    device_type       VARCHAR(50),
    device_user_type  VARCHAR(50),
    device_version    VARCHAR(100),

    created_at        TIMESTAMP(6),
    modified_at       TIMESTAMP(6),

    PRIMARY KEY (process_id)
);

CREATE TABLE communication_log_details_rd
(
    id                BIGINT     NOT NULL AUTO_INCREMENT,

    process_id        BINARY(16) NOT NULL,

    device_id         VARCHAR(255),

    process_type      VARCHAR(50),
    service_type      VARCHAR(50),

    created_by        VARCHAR(255),
    updated_by        VARCHAR(255),
    department_code   VARCHAR(255),

    source_service    VARCHAR(255),
    target_service    VARCHAR(255),

    request           LONGTEXT,
    response          LONGTEXT,
    fault_description LONGTEXT,

    created_at        TIMESTAMP(6),
    updated_at        TIMESTAMP(6),

    PRIMARY KEY (id),
    KEY               idx_comm_log_rd_process_id (process_id),

    CONSTRAINT fk_comm_log_process_rd
        FOREIGN KEY (process_id)
            REFERENCES process_logs_rd (process_id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
);
