ALTER TABLE department
    ADD COLUMN IF NOT EXISTS operator_id BIGINT NULL;

ALTER TABLE department_aud
    ADD COLUMN IF NOT EXISTS operator_id BIGINT NULL;

ALTER TABLE department
    DROP COLUMN country;
