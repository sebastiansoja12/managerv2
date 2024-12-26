ALTER TABLE depot
    RENAME COLUMN depot_code TO department_code;

ALTER TABLE supplier
    RENAME COLUMN depot_code TO department_code;

ALTER TABLE users
    RENAME COLUMN depot_code TO department_code;

ALTER TABLE delivery
    RENAME COLUMN depot_code TO department_code;