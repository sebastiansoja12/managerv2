ALTER TABLE department
    ADD COLUMN admin_user_id BIGINT,
    ADD COLUMN created_by BIGINT NOT NULL,
    ADD COLUMN last_modified_by BIGINT;

ALTER TABLE department
    change nip tax_id varchar(255) not null;
