INSERT INTO shipment (shipment_id, first_name, last_name, sender_telephone, sender_email, sender_city, sender_street,
                      sender_postal_code, sender_country_code, recipient_first_name, recipient_last_name,
                      recipient_email, recipient_telephone, recipient_city, recipient_street, recipient_postal_code,
                      recipient_country_code, status, type, created_at, updated_at, locked, shipment_priority,
                      service_level, signature_required, price_amount, price_currency, length, width, height,
                      length_unit, weight_value, weight_unit, external_id, tracking_number, operator_id)
VALUES (900000001, 'Jan', 'Nadawca', '+48111222333', 'nadawca@manager.local', 'Katowice', 'Nadawcza 1', '40-001', 'PL',
        'Anna', 'Odbiorca', 'odbiorca@manager.local', '+48444555666', 'Katowice', 'Odbiorcza 2', '40-002', 'PL',
        'SENT', 'PARENT', now(), now(), false, 'MEDIUM', 'STANDARD', false, 25.00, 'PLN', 30.000, 20.000, 10.000,
        'CM', 2.500, 'KG', '3b0f6d1e-6a2c-4f8e-9d5b-2e7a1c9f0001', 'E2E-REJECT-0001', 1);

INSERT INTO delivery (id, shipment_id, target_type, target_id, delivery_type, created, delivery_status, status,
                      origin_department_id, destination_department_id, signature_required, operator_id)
VALUES ('e2e-reject-delivery-0001', 900000001, 'SHIPMENT', '900000001', 'OUTBOUND', now(), 0, 'CREATED', 1, 1, false, 1);

INSERT INTO delivery_step (id, delivery_id, step_number, attempted_at, outcome, delivery_status, comment)
VALUES ('6f1c5e2a-0e7b-4c39-9a51-0d6f4f0e2001', 'e2e-reject-delivery-0001', 1, now(), 'IN_PROGRESS', 'DEPOT',
        'Shipment created');
