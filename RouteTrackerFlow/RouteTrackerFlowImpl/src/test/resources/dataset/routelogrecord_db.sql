-- Insert data into route_log table
INSERT INTO route_log (id, parcel_id, return_code, fault_description)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 123456, 'ReturnCode1', 'FaultDescription1'),
    ('550e8400-e29b-41d4-a716-446655440002', 789012, 'ReturnCode2', 'FaultDescription2');
-- Add more rows as needed

-- Insert data into route_log_record_detail table
INSERT INTO route_log_record_detail (route_log_record_id, zebra_id, version, created, description, process_type, request, parcel_status, username, depot_code, supplier_code)
VALUES
    ('550e8400-e29b-41d4-a716-446655440001', 123, 'v1.0', '2023-01-01 12:00:00', 'Detail Description 1', 0, 'Request Detail 1', 0, 's-soja', 'KT1', 'abc'),
    ('550e8400-e29b-41d4-a716-446655440002', 456, 'v2.0', '2023-02-01 14:30:00', 'Detail Description 2', 0, 'Request Detail 2', 0, 's-soja', 'KT1', 'abc');
-- Add more rows as needed
