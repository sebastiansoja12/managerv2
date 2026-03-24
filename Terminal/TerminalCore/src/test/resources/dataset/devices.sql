INSERT INTO terminals (
    device_id,
    external_device_id,
    device_type,
    status,
    created_at,
    updated_at,
    user_id,
    department_code,
    version,
    app_version,
    last_update,
    active
) VALUES (
    101000000000000001,
    'ext-101000000000000001',
    'TERMINAL',
    'ACTIVE',
    '2024-12-29 12:00:00',
    '2024-12-29 12:00:00',
    1001,
    'KT1',
    '1.0.0',
    '1.0.0',
    '2024-12-29 12:00:00',
    true
);

INSERT INTO device_pair (
    device_pair_id,
    device_id,
    paired,
    pair_time,
    error_description,
    pair_key
) VALUES (
    1,
    101000000000000001,
    true,
    '2024-12-29 12:00:00',
    'No errors',
    'ABC123'
);

INSERT INTO device_version (id, device_type, version, device_id, last_update)
VALUES
    (1, 'TERMINAL', '1.0.0', 101000000000000001, '2024-12-29 12:00:00');
