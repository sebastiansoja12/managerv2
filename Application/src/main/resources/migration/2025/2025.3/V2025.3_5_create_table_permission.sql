CREATE TABLE role_permissions
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);



CREATE TABLE users_role_permissions
(
    user_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, permission_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_permission
        FOREIGN KEY (permission_id) REFERENCES role_permissions (id) ON DELETE CASCADE
);


ALTER TABLE users_role_permissions
    ADD CONSTRAINT fk_user
        FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE;

ALTER TABLE users_role_permissions
    ADD CONSTRAINT fk_permission
        FOREIGN KEY (permission_id) REFERENCES role_permissions (id) ON DELETE CASCADE;


INSERT INTO role_permissions (name)
VALUES ('ROLE_ADMIN_READ'),
       ('ROLE_ADMIN_UPDATE'),
       ('ROLE_ADMIN_CREATE'),
       ('ROLE_ADMIN_DELETE'),
       ('ROLE_MANAGER_READ'),
       ('ROLE_MANAGER_UPDATE'),
       ('ROLE_MANAGER_CREATE'),
       ('ROLE_MANAGER_DELETE'),
       ('ROLE_SUPPLIER_READ'),
       ('ROLE_SUPPLIER_UPDATE'),
       ('ROLE_SUPPLIER_CREATE'),
       ('ROLE_SUPPLIER_DELETE');
