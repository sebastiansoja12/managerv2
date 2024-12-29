INSERT INTO device (
    device_id,
    version,
    user_id,
    department_code,
    device_type,
    last_update,
    active
) VALUES (
             1,
             '1.0.0',
             101,
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
