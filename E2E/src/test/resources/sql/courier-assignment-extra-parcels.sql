INSERT INTO shipment (shipment_id, first_name, last_name, sender_telephone, sender_email, sender_city, sender_street,
                      sender_postal_code, sender_country_code, recipient_first_name, recipient_last_name,
                      recipient_email, recipient_telephone, recipient_city, recipient_street, recipient_postal_code,
                      recipient_country_code, status, type, created_at, updated_at, locked, shipment_priority,
                      service_level, signature_required, price_amount, price_currency, length, width, height,
                      length_unit, weight_value, weight_unit, external_id, tracking_number, payment_state, operator_id)
SELECT 930000 + n, 'Jan', 'Nadawca', '+48111222333', 'nadawca@manager.local', 'Katowice', 'Nadawcza 1', '40-001', 'PL',
       'Anna', 'Odbiorca', 'odbiorca@manager.local', '+48444555666', 'Katowice', 'Odbiorcza 2', '40-777', 'PL',
       'SENT', 'PARENT', now(), now(), false, 'MEDIUM', 'STANDARD', false, 25.00, 'PLN', 30.000, 20.000, 10.000,
       'CM', 2.500, 'KG', CAST(gen_random_uuid() AS VARCHAR), 'E2E-COURIER-' || n, 'NOT_REQUIRED', 1
FROM generate_series(31, 60) AS n;

INSERT INTO delivery (id, shipment_id, target_type, target_id, delivery_type, created, delivery_status, status,
                      origin_department_id, destination_department_id, current_department_id,
                      current_department_since, delivery_method, signature_required, operator_id)
SELECT 'e2e-courier-delivery-' || n, 930000 + n, 'SHIPMENT', CAST(930000 + n AS VARCHAR), 'OUTBOUND',
       now() - interval '1 day', 0, 'MODIFIED', 2, 1, 1,
       CASE WHEN n = 31 THEN now() - interval '20 hours'
            WHEN n BETWEEN 41 AND 46 THEN now() - interval '50 minutes' + (n - 41) * interval '1 minute'
            ELSE now() - interval '5 minutes' END,
       'COURIER', false, 1
FROM generate_series(31, 60) AS n;

INSERT INTO delivery_step (id, delivery_id, step_number, attempted_at, outcome, delivery_status, department_id, comment)
SELECT gen_random_uuid(), 'e2e-courier-delivery-' || n, 1, now() - interval '20 hours', 'IN_PROGRESS', 'DEPOT', 1,
       'Accepted in department'
FROM generate_series(31, 60) AS n;
