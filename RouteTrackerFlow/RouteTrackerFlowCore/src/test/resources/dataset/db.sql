-- INSERT INTO statement to insert sample data
INSERT INTO DEPOT (depot_code, city, street, country)
VALUES
    ('KT1', 'City1', 'Street1', 'Country1'),
    ('KT2', 'City2', 'Street2', 'Country2'),
    ('WA1', 'City3', 'Street3', 'Country3');

-- Insert fake data into PARCEL table
INSERT INTO PARCEL (id, first_name, last_name, sender_email, sender_city, sender_street, sender_postal_code,
                    recipient_city, recipient_email, recipient_first_name, recipient_last_name, recipient_postal_code,
                    recipient_street, recipient_telephone, price, parcel_size, status, destination, parcel_related_id, createdAt, updatedAt)
VALUES (1001, 'John', 'Doe', 'john@example.com', 'SenderCity1', 'SenderStreet1', '12345', 'RecipientCity1',
        'recipient1@example.com', 'RecipientFirstName1', 'RecipientLastName1', '54321', 'RecipientStreet1',
        '1234567890', 25.00, 0, 1, 'WA1', null, '2023-10-23T17:27:49.254626', '2023-10-23T17:27:49.254626'),
       (1002, 'Jane', 'Smith', 'jane@example.com', 'SenderCity2', 'SenderStreet2', '54321', 'RecipientCity2',
        'recipient2@example.com', 'RecipientFirstName2', 'RecipientLastName2', '12345', 'RecipientStreet2',
        '9876543210', 35.00, 0, 2, 'KT2', null, '2023-10-23T17:27:49.254626', '2023-10-23T17:27:49.254626'),
       (1003, 'Alice', 'Johnson', 'alice@example.com', 'SenderCity3', 'SenderStreet3', '98765', 'RecipientCity3',
        'recipient3@example.com', 'RecipientFirstName3', 'RecipientLastName3', '67890', 'RecipientStreet3',
        '1237894560', 45.00, 0, 3, 'GD1', null, '2023-10-23T17:27:49.254626', '2023-10-23T17:27:49.254626');


-- Insert fake data into USERS table
INSERT INTO USERS (id, email, first_name, last_name, password, role, username, depot_code)
VALUES (1, 'user1@example.com', 'John', 'Doe', 'password1', 'User', 'johndoe', 'KT1'),
       (2, 'user2@example.com', 'Jane', 'Smith', 'password2', 'User', 'janesmith', 'KT2'),
       (3, 'admin@example.com', 'Admin', 'Admin', 'adminpassword', 'Admin', 'adminuser', 'WA1');

-- Insert fake data into SUPPLIER table
INSERT INTO SUPPLIER (supplier_code, first_name, last_name, username, depot_code)
VALUES ('SUP001', 'Supplier1', 'LastName1', 'supplier1user', 'KT1'),
       ('SUP002', 'Supplier2', 'LastName2', 'supplier2user', 'KT2'),
       ('SUP003', 'Supplier3', 'LastName3', 'supplier3user', 'WA1');

INSERT INTO ROUTE (id, created, depot_code, parcel_id, supplier_code, user_id)
VALUES ('45e8c3b4-79b2-4d29-b9c3-1d5e36a6e84a', '2023-10-09 08:00:00', 'KT1', 1001, 'SUP001', 1),
       ('a3df8e5c-35ab-4a5f-b3e0-6d0d09b74a7f', '2023-10-09 09:30:00', 'KT2', 1002, 'SUP002', 2),
       ('f97e71e0-1b57-48e3-9a28-74225dbdde0d', '2023-10-09 10:45:00', 'WA1', 1003, 'SUP003', 3);
