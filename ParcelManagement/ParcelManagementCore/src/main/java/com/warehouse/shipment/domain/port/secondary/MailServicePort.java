package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.Notification;

public interface MailServicePort {
    void sendShipmentNotification(Notification notification);
}