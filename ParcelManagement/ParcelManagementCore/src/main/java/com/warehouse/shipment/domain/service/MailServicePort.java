package com.warehouse.shipment.domain.service;

import com.warehouse.shipment.domain.vo.Notification;

public interface MailServicePort {
    void sendShipmentNotification(Notification notification);
}
