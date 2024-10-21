CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          title VARCHAR(255) NOT NULL,
                          shipment_status ENUM('CREATED', 'REROUTE', 'SENT', 'DELIVERY', 'RETURN', 'REDIRECT') NOT NULL,
                          language VARCHAR(10) NOT NULL,
                          message_content TEXT NOT NULL,
                          created_at DATETIME NOT NULL,
                          updated_at DATETIME DEFAULT NULL,
                          sender VARCHAR(255) DEFAULT NULL
);

-- Status: CREATED
INSERT INTO messages (title, shipment_status, language, message_content, created_at, updated_at, sender)
VALUES
    ('Package Created', 'CREATED', 'EN', 'Your package has been created and is awaiting further processing.', '2024-10-21 10:00:00', NULL, 'SenderName'),
    ('Paczka Utworzona', 'CREATED', 'PL', 'Twoja paczka została utworzona i czeka na dalsze przetwarzanie.', '2024-10-21 10:00:00', NULL, 'SenderName'),
    ('Colis Créé', 'CREATED', 'FR', 'Votre colis a été créé et attend un traitement ultérieur.', '2024-10-21 10:00:00', NULL, 'SenderName'),
    ('Paket Erstellt', 'CREATED', 'DE', 'Ihr Paket wurde erstellt und wartet auf die weitere Bearbeitung.', '2024-10-21 10:00:00', NULL, 'SenderName');

-- Status: REROUTE
INSERT INTO messages (title, shipment_status, language, message_content, created_at, updated_at, sender)
VALUES
    ('Package Rerouted', 'REROUTE', 'EN', 'Your package has been rerouted to a new address.', '2024-10-21 11:00:00', NULL, 'SenderName'),
    ('Paczka Przekierowana', 'REROUTE', 'PL', 'Twoja paczka została przekierowana na nowy adres.', '2024-10-21 11:00:00', NULL, 'SenderName'),
    ('Colis Redirigé', 'REROUTE', 'FR', 'Votre colis a été redirigé vers une nouvelle adresse.', '2024-10-21 11:00:00', NULL, 'SenderName'),
    ('Paket Umgeleitet', 'REROUTE', 'DE', 'Ihr Paket wurde an eine neue Adresse umgeleitet.', '2024-10-21 11:00:00', NULL, 'SenderName');

-- Status: SENT
INSERT INTO messages (title, shipment_status, language, message_content, created_at, updated_at, sender)
VALUES
    ('Package Sent', 'SENT', 'EN', 'Your package has been shipped and is on its way.', '2024-10-21 12:00:00', NULL, 'SenderName'),
    ('Paczka Wysłana', 'SENT', 'PL', 'Twoja paczka została wysłana i jest w drodze.', '2024-10-21 12:00:00', NULL, 'SenderName'),
    ('Colis Envoyé', 'SENT', 'FR', 'Votre colis a été envoyé et est en route.', '2024-10-21 12:00:00', NULL, 'SenderName'),
    ('Paket Verschickt', 'SENT', 'DE', 'Ihr Paket wurde versendet und ist unterwegs.', '2024-10-21 12:00:00', NULL, 'SenderName');

-- Status: DELIVERY
INSERT INTO messages (title, shipment_status, language, message_content, created_at, updated_at, sender)
VALUES
    ('Out for Delivery', 'DELIVERY', 'EN', 'Your package is out for delivery and will arrive soon.', '2024-10-21 13:00:00', NULL, 'SenderName'),
    ('W Drodze', 'DELIVERY', 'PL', 'Twoja paczka jest w drodze i wkrótce dotrze do celu.', '2024-10-21 13:00:00', NULL, 'SenderName'),
    ('En Cours de Livraison', 'DELIVERY', 'FR', 'Votre colis est en cours de livraison et arrivera bientôt.', '2024-10-21 13:00:00', NULL, 'SenderName'),
    ('Zustellung Läuft', 'DELIVERY', 'DE', 'Ihr Paket wird zugestellt und kommt bald an.', '2024-10-21 13:00:00', NULL, 'SenderName');

-- Status: RETURN
INSERT INTO messages (title, shipment_status, language, message_content, created_at, updated_at, sender)
VALUES
    ('Package Returned', 'RETURN', 'EN', 'Your package is being returned to the sender.', '2024-10-21 14:00:00', NULL, 'SenderName'),
    ('Paczka Zwrócona', 'RETURN', 'PL', 'Twoja paczka jest zwracana do nadawcy.', '2024-10-21 14:00:00', NULL, 'SenderName'),
    ('Colis Renvoyé', 'RETURN', 'FR', 'Votre colis est en cours de retour vers l\'expéditeur.', '2024-10-21 14:00:00', NULL, 'SenderName'),
    ('Paket Zurückgeschickt', 'RETURN', 'DE', 'Ihr Paket wird an den Absender zurückgeschickt.', '2024-10-21 14:00:00', NULL, 'SenderName');

-- Status: REDIRECT
INSERT INTO messages (title, shipment_status, language, message_content, created_at, updated_at, sender)
VALUES
    ('Package Redirected', 'REDIRECT', 'EN', 'Your package has been redirected to another location as requested.', '2024-10-21 15:00:00', NULL, 'SenderName'),
    ('Paczka Przekierowana', 'REDIRECT', 'PL', 'Twoja paczka została przekierowana do innej lokalizacji na żądanie.', '2024-10-21 15:00:00', NULL, 'SenderName'),
    ('Colis Réorienté', 'REDIRECT', 'FR', 'Votre colis a été réorienté vers un autre emplacement comme demandé.', '2024-10-21 15:00:00', NULL, 'SenderName'),
    ('Paket Umgeleitet', 'REDIRECT', 'DE', 'Ihr Paket wurde wie gewünscht an einen anderen Standort umgeleitet.', '2024-10-21 15:00:00', NULL, 'SenderName');
