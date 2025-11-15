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
VALUES ('ADMIN_READ'),
       ('ADMIN_UPDATE'),
       ('ADMIN_CREATE'),
       ('ADMIN_DELETE'),
       ('MANAGER_READ'),
       ('MANAGER_UPDATE'),
       ('MANAGER_CREATE'),
       ('MANAGER_DELETE'),
       ('SUPPLIER_READ'),
       ('SUPPLIER_UPDATE'),
       ('SUPPLIER_CREATE'),
       ('SUPPLIER_DELETE');
