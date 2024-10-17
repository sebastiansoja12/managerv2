package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.shipment.domain.port.secondary.MailServicePort;
import com.warehouse.shipment.domain.model.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailPort mailPort;

    private final NotificationMapper notificationMapper;

    @Override
    public void sendShipmentNotification(Notification notification) {
        final com.warehouse.mail.domain.vo.Notification shipmentNotification = notificationMapper.map(notification);
        mailPort.sendNotification(shipmentNotification);
    }

    @Override
    public void sendRerouteNotification(Notification notification) {
        final com.warehouse.mail.domain.vo.Notification rerouteNotification = notificationMapper.map(notification);
        mailPort.sendNotification(rerouteNotification);
    }
}
