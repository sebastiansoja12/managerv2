package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.mail.infrastructure.adapter.primary.event.MailTemplateNotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.reroute.application.port.secondary.RerouteNotificationServicePort;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.ReroutableShipment;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public class RerouteNotificationServiceAdapter implements RerouteNotificationServicePort {

    private static final String REROUTE_TOKEN_TEMPLATE = "shipment.reroute.token";

    private final NotificationEventPublisher notificationEventPublisher;

    private final String shipmentEditUrl;

    public RerouteNotificationServiceAdapter(final NotificationEventPublisher notificationEventPublisher,
                                             final String shipmentEditUrl) {
        this.notificationEventPublisher = notificationEventPublisher;
        this.shipmentEditUrl = shipmentEditUrl;
    }

    @Override
    public void sendRerouteToken(final ReroutableShipment shipment, final RerouteToken rerouteToken) {
        final String trackingNumber = shipment.trackingNumber().value();
        final long validMinutes = Math.max(1,
                Duration.between(Instant.now(), rerouteToken.getExpiresAt()).toMinutes());
        final Map<String, String> variables = Map.of(
                "trackingNumber", trackingNumber,
                "code", rerouteToken.getValue().formatted(),
                "validMinutes", String.valueOf(validMinutes),
                "shipmentEditUrl", shipmentEditUrl + "/" + URLEncoder.encode(trackingNumber, StandardCharsets.UTF_8),
                "firstName", shipment.sender().firstName());
        notificationEventPublisher.send(new MailTemplateNotificationEvent(REROUTE_TOKEN_TEMPLATE,
                rerouteToken.getEmail(),  shipment.sender().countryCode(),
                variables));
    }
}
