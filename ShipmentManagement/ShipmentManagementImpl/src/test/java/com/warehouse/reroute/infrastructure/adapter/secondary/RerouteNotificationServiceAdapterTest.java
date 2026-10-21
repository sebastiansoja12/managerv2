package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mail.infrastructure.adapter.primary.event.MailTemplateNotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;
import com.warehouse.reroute.domain.model.RerouteToken;
import com.warehouse.reroute.domain.vo.ReroutableShipment;
import com.warehouse.reroute.domain.vo.RerouteParty;
import com.warehouse.reroute.domain.vo.RerouteTokenValue;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RerouteNotificationServiceAdapterTest {

    private final List<MailTemplateNotificationEvent> published = new ArrayList<>();
    private final RerouteNotificationServiceAdapter adapter = new RerouteNotificationServiceAdapter(
            new NotificationEventPublisher() {
                @Override
                public void send(final NotificationEvent notificationEvent) {
                }

                @Override
                public void send(final MailTemplateNotificationEvent mailTemplateNotificationEvent) {
                    published.add(mailTemplateNotificationEvent);
                }
            }, "https://manager.example/shipment-edit");

    @Test
    void shouldPublishTemplatedRerouteTokenNotificationInSenderCountry() {
        final ReroutableShipment shipment = new ReroutableShipment(new ShipmentId(1L), new TrackingNumber("PL 1/2"),
                ShipmentStatus.CREATED, true,
                new RerouteParty("Hans", "Muller", "hans@example.de", "123", "Berlin", "10115", "Unter den Linden 1", "DE"),
                null);
        final RerouteToken token = new RerouteToken(null, new ShipmentId(1L), new RerouteTokenValue(42),
                "hans@example.de", Instant.now(), Instant.now().plus(Duration.ofMinutes(15)), 0, null);

        adapter.sendRerouteToken(shipment, token);

        assertEquals(1, published.size());
        final MailTemplateNotificationEvent event = published.get(0);
        assertEquals("shipment.reroute.token", event.templateKey());
        assertEquals("hans@example.de", event.recipientEmail());
        assertEquals("DE", event.recipientCountryCode());
        assertEquals("000042", event.variables().get("code"));
        assertEquals("Hans", event.variables().get("firstName"));
        assertEquals("https://manager.example/shipment-edit/PL+1%2F2", event.variables().get("shipmentEditUrl"));
        assertTrue(Integer.parseInt(event.variables().get("validMinutes")) >= 14);
    }
}
