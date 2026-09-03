package com.warehouse.reroute.infrastructure.adapter.secondary;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationDto;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.reroute.application.port.secondary.RerouteNotificationServicePort;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.ReroutableShipment;

public class RerouteNotificationServiceAdapter implements RerouteNotificationServicePort {

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
        final String body = """
                Dzień dobry,

                otrzymaliśmy prośbę o zmianę danych przesyłki %s.

                Kod weryfikacyjny: %s

                Kod jest ważny przez %d min i można go użyć tylko raz. Wpisz go na stronie edycji przesyłki:
                %s

                Jeśli to nie Ty prosiłeś o zmianę danych, zignoruj tę wiadomość.
                """.formatted(trackingNumber, rerouteToken.getValue().formatted(), validMinutes,
                shipmentEditUrl + "/" + URLEncoder.encode(trackingNumber, StandardCharsets.UTF_8));
        notificationEventPublisher.send(new NotificationEvent(new NotificationDto(
                "Kod do zmiany danych przesyłki " + trackingNumber, body, rerouteToken.getEmail())));
    }
}
