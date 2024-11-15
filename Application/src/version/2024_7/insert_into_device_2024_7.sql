INSERT INTO device (id, version, user_id, depot_code, device_type, active, last_update)
VALUES
    (2137, '1.0.0', 1, 'KT1', 'TERMINAL', true, '2024-11-15 12:00:00'),
    (2138, '1.0.0', 2, 'KT2', 'TERMINAL', true, '2024-11-15 13:00:00');

INSERT INTO device_pair (device_pair_id, device_entity_id, paired, pair_time, error_description, pair_key)
VALUES
    (1, 2137, true, '2024-11-15 12:00:00', NULL, 'PAIR001'),
    (2, 2138, true, '2024-11-15 15:00:00', NULL, 'PAIR002');
INSERT INTO device_version (id, device_type, version, device_id, last_update)
VALUES
    (1, 'TERMINAL', '1.0.0', '2137', '2024-11-15 12:00:00'),
    (2, 'TERMINAL', '1.0.0', '2138', '2024-11-15 13:30:00');
