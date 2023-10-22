package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.port.primary.MailPort;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.port.secondary.MailServicePort;
import com.warehouse.reroute.domain.model.Notification;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.NotificationMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MailAdapter implements MailServicePort {

    private final MailPort mailPort;

    private final NotificationMapper notificationMapper;


    @Override
    public void sendReroutingInformation(RerouteToken rerouteToken) {
        final Notification rerouteNotification = buildNotification(rerouteToken);
        final com.warehouse.mail.domain.vo.Notification notification = notificationMapper.map(rerouteNotification);
        mailPort.sendNotification(notification);
    }

    public Notification buildNotification(RerouteToken rerouteToken) {
        return Notification.builder()
                .body("Prosimy wejść w link: " + "http://localhost:4200/reroute-edit/" + rerouteToken.getParcelId()
                        + "/" + rerouteToken.getToken())
                .recipient(rerouteToken.getEmail())
                .subject("Zmiana danych przesyłki: " + rerouteToken.getParcelId())
                .build();
    }
}
