package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.shipment.domain.service.MailServicePort;
import com.warehouse.shipment.domain.vo.Notification;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.NotificationMapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailService mailService;

    private final NotificationMapper notificationMapper;

    @Override
    public void sendShipmentNotification(Notification notification) {
        final com.warehouse.mail.domain.vo.Notification shipmentNotification = notificationMapper.map(notification);
        mailService.sendNotification(shipmentNotification);
    }
}
