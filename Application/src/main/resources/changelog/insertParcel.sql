-- Sample data for the 'parcel' table

-- Inserting data for the 'parcel' table
INSERT INTO SHIPMENT (id, first_name, last_name, recipient_city, recipient_email, recipient_first_name,
                    recipient_last_name, recipient_postal_code, recipient_street, recipient_telephone,
                    sender_city, sender_email, sender_postal_code, sender_street, sender_telephone,
                    destination, status, parcel_size, parent_related_id, type, created_at, updated_at)
VALUES
    (1000000000, 'John', 'Doe', 'RecipientCity1', 'recipient1@email.com', 'RecipientFirstName1', 'RecipientLastName1', '12345', 'RecipientStreet1', '123-456-7890',
     'SenderCity1', 'sender1@email.com', '54321', 'SenderStreet1', '987-654-3210',
     'KT1', 1, 2, NULL, 3, CURRENT_TIME, CURRENT_TIME),

    (1000000001, 'Jane', 'Doe', 'RecipientCity2', 'recipient2@email.com', 'RecipientFirstName2', 'RecipientLastName2', '67890', 'RecipientStreet2', '987-654-3210',
     'SenderCity2', 'sender2@email.com', '09876', 'SenderStreet2', '123-456-7890',
     'KT2', 2, 1, NULL, 3, CURRENT_TIME, CURRENT_TIME),

    (1000000002, 'Alice', 'Smith', 'RecipientCity3', 'recipient3@email.com', 'RecipientFirstName3', 'RecipientLastName3', '54321', 'RecipientStreet3', '456-789-0123',
     'SenderCity3', 'sender3@email.com', '56789', 'SenderStreet3', '789-012-3456',
     'GD1', 3, 3, NULL, 1, CURRENT_TIME, CURRENT_TIME),

    (1000000003, 'Bob', 'Johnson', 'RecipientCity4', 'recipient4@email.com', 'RecipientFirstName4', 'RecipientLastName4', '98765', 'RecipientStreet4', '234-567-8901',
     'SenderCity4', 'sender4@email.com', '45678', 'SenderStreet4', '012-345-6789',
     'WA3', 1, 1, NULL, 2, CURRENT_TIME, CURRENT_TIME),

    (1000000004, 'Eva', 'Brown', 'RecipientCity5', 'recipient5@email.com', 'RecipientFirstName5', 'RecipientLastName5', '45678', 'RecipientStreet5', '890-123-4567',
     'SenderCity5', 'sender5@email.com', '34567', 'SenderStreet5', '345-678-9012',
     'ZGR', 2, 2, NULL, 1, CURRENT_TIME, CURRENT_TIME),

    (1000000005, 'David', 'Williams', 'RecipientCity6', 'recipient6@email.com', 'RecipientFirstName6', 'RecipientLastName6', '23456', 'RecipientStreet6', '012-345-6789',
     'SenderCity6', 'sender6@email.com', '65432', 'SenderStreet6', '678-901-2345',
     'NCS', 3, 1, NULL, 2, CURRENT_TIME, CURRENT_TIME),

    (1000000006, 'Grace', 'Jones', 'RecipientCity7', 'recipient7@email.com', 'RecipientFirstName7', 'RecipientLastName7', '76543', 'RecipientStreet7', '789-012-3456',
     'SenderCity7', 'sender7@email.com', '54321', 'SenderStreet7', '901-234-5678',
     'KT3', 1, 3, NULL, 3, CURRENT_TIME, CURRENT_TIME),

    (1000000007, 'Frank', 'Miller', 'RecipientCity8', 'recipient8@email.com', 'RecipientFirstName8', 'RecipientLastName8', '34567', 'RecipientStreet8', '456-789-0123',
     'SenderCity8', 'sender8@email.com', '87654', 'SenderStreet8', '234-567-8901',
     'KT4', 2, 2, NULL, 1, CURRENT_TIME, CURRENT_TIME),

    (1000000008, 'Sophia', 'Taylor', 'RecipientCity9', 'recipient9@email.com', 'RecipientFirstName9', 'RecipientLastName9', '87654', 'RecipientStreet9', '901-234-5678',
     'SenderCity9', 'sender9@email.com', '23456', 'SenderStreet9', '567-890-1234',
     'KR1', 3, 1, NULL, 2, CURRENT_TIME, CURRENT_TIME),

    (1000000009, 'Jack', 'Clark', 'RecipientCity10', 'recipient10@email.com', 'RecipientFirstName10', 'RecipientLastName10', '65432', 'RecipientStreet10', '345-678-9012',
     'SenderCity10', 'sender10@email.com', '76543', 'SenderStreet10', '890-123-4567',
     'RZE', 1, 3, NULL, 3, CURRENT_TIME, CURRENT_TIME);

-- Add more INSERT statements for additional data as needed
