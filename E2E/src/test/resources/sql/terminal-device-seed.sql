INSERT INTO operator_configurations (operator_id, supports_domestic_shipping, supports_international_shipping,
                                     supports_express_shipping, supports_same_day_delivery, supports_cash_on_delivery,
                                     supports_parcel_lockers, supports_pickup_points, supports_home_delivery,
                                     supports_saturday_delivery, supports_sunday_delivery, supports_return_shipments,
                                     provides_tracking, provides_insurance, max_weight, min_weight, max_length,
                                     max_width, max_height, max_shipment_value, min_delivery_days, max_delivery_days,
                                     express_delivery_days, same_day_delivery_hours, international_delivery_days)
VALUES (1, true, true, true, false, true, true, true, true, false, false, true, true, false, 30.0, 0.1, 200.0, 200.0,
        200.0, 10000.0, 1, 3, 1, 0, 7)
ON CONFLICT DO NOTHING;

INSERT INTO users (user_id, username, password, first_name, last_name, email, role, department_id, deleted,
                   created_at, updated_at, language, initial, operator_id)
VALUES (1001, 'e2e.terminal@manager.local', '$2a$10$GIi6ypLfEcfc.9Q9R99IAeeZn9cCL0YpG8fUEIKijN.hs4koSdE5y',
        'Terminal', 'E2E', 'e2e.terminal@manager.local', 'USER', 1, false, now(), now(), 'pl', false, 1)
ON CONFLICT DO NOTHING;

INSERT INTO terminals (device_id, external_device_id, device_type, status, created_at, updated_at, user_id,
                       department_code, version, last_update, active, operator_id)
VALUES ('TL:e2e-terminal-0001', 'E2E-TERMINAL-0001', 'TERMINAL', 'ACTIVE', now(), now(), 1001, 'KT1', '1.0.0',
        now(), true, 1)
ON CONFLICT DO NOTHING;

INSERT INTO device_pair (device_pair_id, device_id, paired, pair_time, pair_key, pair_key_expires_at)
VALUES (900001, 'TL:e2e-terminal-0001', true, now(), 'e2e-terminal-0001-pair-key', now() + interval '2 days')
ON CONFLICT DO NOTHING;
