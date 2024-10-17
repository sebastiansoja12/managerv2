package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Notification;

public interface MailServicePort {
    void sendShipmentNotification(Notification notification);
    void sendRerouteNotification(Notification notification);
}
