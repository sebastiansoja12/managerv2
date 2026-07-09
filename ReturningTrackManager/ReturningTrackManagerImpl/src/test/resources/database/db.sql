INSERT INTO returning_return_package (
    return_id,
    shipment_id,
    reason,
    return_status,
    return_token,
    assigned_department,
    returned_department,
    assigned_to,
    processed_by,
    reason_code,
    created_at,
    updated_at
) VALUES
      (1001, 5001, 'Zwrot — niepotrzebny', 'PENDING', 'ABC123TOKEN', 'DEP01', 'DEP02', 1, 2, 'NO_LONGER_NEEDED', '2025-10-12 12:00:00', '2025-10-12 12:00:00'),
      (1002, 5002, 'Uszkodzony produkt', 'PROCESSED', 'XYZ789TOKEN', 'DEP01', 'DEP03', 1, 2, 'DAMAGED', '2025-10-12 12:00:00', '2025-10-12 12:30:00');
