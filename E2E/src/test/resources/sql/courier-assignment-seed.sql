INSERT INTO department_configuration_override (department_id, operator_id, department_courier_assignment_enabled,
                                               department_courier_assignment_date_field,
                                               department_courier_assignment_window_type,
                                               department_courier_assignment_window_start,
                                               department_courier_assignment_window_end,
                                               department_courier_assignment_time_zone,
                                               department_courier_assignment_processing_delay_minutes,
                                               department_courier_assignment_overlap_minutes,
                                               department_courier_assignment_max_window_hours,
                                               department_courier_assignment_lookback_days,
                                               department_courier_assignment_batch_size)
VALUES (1, 1, true, 'DESTINATION_ARRIVAL', 'ROLLING', '00:00:00', '00:00:00', NULL, 0, 15, 96, 3, 5)
ON CONFLICT (department_id) DO UPDATE SET department_courier_assignment_enabled = true,
                                          department_courier_assignment_date_field = 'DESTINATION_ARRIVAL',
                                          department_courier_assignment_window_type = 'ROLLING',
                                          department_courier_assignment_processing_delay_minutes = 0,
                                          department_courier_assignment_overlap_minutes = 15,
                                          department_courier_assignment_max_window_hours = 96,
                                          department_courier_assignment_lookback_days = 3,
                                          department_courier_assignment_batch_size = 5;

INSERT INTO suppliers (supplier_id, supplier_code, department_id, first_name, last_name, status, area_name, city,
                       country, created_at, operator_id)
VALUES (920001, 'e2eCourierA', 1, 'Adam', 'Kurier', 'ACTIVE', 'Katowice Centrum', 'Katowice', 'PL', now(), 1),
       (920002, 'e2eCourierB', 1, 'Beata', 'Kurier', 'ACTIVE', 'Katowice Centrum', 'Katowice', 'PL', now(), 1),
       (920003, 'e2eCourierC', 1, 'Cezary', 'Kurier', 'ACTIVE', 'Katowice Centrum', 'Katowice', 'PL', now(), 1);

INSERT INTO delivery_area_postal_codes (delivery_area_id, postal_code)
VALUES (920001, '40-777'),
       (920002, '40-777'),
       (920003, '40-777');

INSERT INTO supplier_configurations (supplier_id, max_delivery_attempts, preferred_label_format,
                                     default_shipment_sort_order, created_at, operator_id)
VALUES (920001, 3, 'PDF_A6', 'DELIVERY_ORDER', now(), 1),
       (920002, 3, 'PDF_A6', 'DELIVERY_ORDER', now(), 1),
       (920003, 3, 'PDF_A6', 'DELIVERY_ORDER', now(), 1);

INSERT INTO supplier_configuration_settings (supplier_id, setting, enabled)
VALUES (920001, 'AUTO_ASSIGN_SHIPMENTS_BY_AREA', true),
       (920002, 'AUTO_ASSIGN_SHIPMENTS_BY_AREA', true),
       (920003, 'AUTO_ASSIGN_SHIPMENTS_BY_AREA', false);
