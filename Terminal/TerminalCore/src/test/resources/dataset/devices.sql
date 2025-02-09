INSERT INTO device (
    device_id,
    version,
    username,
    department_code,
    device_type,
    last_update,
    active
) VALUES (
             1,
             '1.0.0',
             's-soja',
             'KT1',
             'TERMINAL',
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
             1,
             true,
             '2024-12-29 12:00:00',
             'No errors',
             'ABC123'
         );


INSERT INTO device_version (id, device_type, version, device_id, last_update)
VALUES
    (1, 'TERMINAL', '1.0.0', '1', '2024-12-29 12:00:00');
