package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.mail.domain.service.MailService;
import com.warehouse.mail.domain.vo.Notification;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.port.secondary.RerouteTokenServicePort;
import com.warehouse.reroute.domain.vo.RerouteNotification;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.RequestMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenAdapter implements RerouteTokenServicePort {

    private final RequestMapper requestMapper;

    private final MailService mailService;

    @Override
    public void sendReroutingInformation(RerouteToken rerouteToken) {
        final RerouteNotification rerouteNotification = buildNotification(rerouteToken);
        final Notification notification = requestMapper.map(rerouteNotification);
        mailService.sendNotification(notification);
    }

    public RerouteNotification buildNotification(RerouteToken rerouteToken) {
        return RerouteNotification.builder()
                .body("Prosimy wejść w link: " + "http://localhost:4200/reroute-edit/" + rerouteToken.getParcelId()
                        + "/" + rerouteToken.getToken())
                .recipient(rerouteToken.getEmail())
                .subject("Zmiana danych przesyłki: " + rerouteToken.getParcelId())
                .build();
    }
}
