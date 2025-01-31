ALTER TABLE device ADD COLUMN username VARCHAR(255);

ALTER TABLE device
    MODIFY COLUMN username VARCHAR(255) NOT NULL,
    ADD CONSTRAINT fk_device_username FOREIGN KEY (username) REFERENCES users(username);

UPDATE device d
    JOIN users u ON d.user_id = u.user_id
SET d.username = u.username;

ALTER TABLE device DROP COLUMN user_id;
