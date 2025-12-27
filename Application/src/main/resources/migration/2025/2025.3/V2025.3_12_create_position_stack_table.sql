CREATE TABLE conf_position_stack
(
    id    BIGSERIAL PRIMARY KEY,
    token TEXT    NOT NULL,
    valid BOOLEAN NOT NULL DEFAULT TRUE
);
