package com.warehouse.mail.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.mail.domain.model.MailRequest;
import com.warehouse.reroute.api.event.ShipmentRerouteRequestedIntegrationEvent;
import com.warehouse.reroute.api.event.snapshot.ReroutePartyData;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static com.warehouse.mail.MailFixture.ENGLISH;
import static com.warehouse.mail.MailFixture.GERMAN;
import static com.warehouse.mail.MailFixture.POLISH;
import static com.warehouse.mail.MailFixture.party;
import static com.warehouse.mail.MailFixture.shipment;
import static com.warehouse.mail.MailFixture.shipmentMailRequestMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipmentMailRequestMapperTest {

    private final ShipmentMailRequestMapper mapper = shipmentMailRequestMapper();

    @Test
    void shouldCreateRequestsForSenderAndRecipientInTheirLanguages() {
        final List<MailRequest> requests = mapper.mapCreated(shipment(ShipmentStatus.CREATED,
                party("Jan", "jan@example.com", "PL"), party("Hans", "hans@example.de", "DE")));

        assertEquals(2, requests.size());
        assertEquals("shipment.created.sender", requests.get(0).templateKey());
        assertEquals(POLISH, requests.get(0).locale());
        assertEquals("jan@example.com", requests.get(0).recipient().value());
        assertEquals("Jan", requests.get(0).variables().get("firstName"));
        assertEquals("shipment.created.recipient", requests.get(1).templateKey());
        assertEquals(GERMAN, requests.get(1).locale());
        assertEquals("Hans", requests.get(1).variables().get("firstName"));
        assertEquals("Jan", requests.get(1).variables().get("senderFirstName"));
        assertEquals("PL 123/45", requests.get(1).variables().get("trackingNumber"));
        assertEquals("https://manager.example/tracking/PL+123%2F45", requests.get(1).variables().get("trackingUrl"));
        assertEquals("https://manager.example/shipment-edit/PL+123%2F45",
                requests.get(1).variables().get("shipmentEditUrl"));
    }

    @Test
    void shouldUseUnmappedCountryLocaleForForeignRecipient() {
        final List<MailRequest> requests = mapper.mapDelivered(shipment(ShipmentStatus.DELIVERY,
                party("Jan", null, "PL"), party("Marie", "marie@example.fr", "FR")));

        assertEquals(1, requests.size());
        assertEquals("shipment.delivered.recipient", requests.get(0).templateKey());
        assertEquals(ENGLISH, requests.get(0).locale());
    }

    @Test
    void shouldSkipPartiesWithoutValidEmail() {
        final List<MailRequest> requests = mapper.mapStatusChanged(shipment(ShipmentStatus.SENT,
                party("Jan", "not-an-email", "PL"), party("Anna", " ", "PL")));

        assertTrue(requests.isEmpty());
    }

    @Test
    void shouldBuildStatusSpecificTemplateKey() {
        final List<MailRequest> requests = mapper.mapStatusChanged(shipment(ShipmentStatus.UNDELIVERABLE,
                party("Jan", "jan@example.com", null), party("Anna", "anna@example.com", "AT")));

        assertEquals("shipment.status.undeliverable.sender", requests.get(0).templateKey());
        assertEquals(POLISH, requests.get(0).locale());
        assertEquals("shipment.status.undeliverable.recipient", requests.get(1).templateKey());
        assertEquals(GERMAN, requests.get(1).locale());
    }

    @Test
    void shouldUseExternalTemplatesForShipmentCreatedByExternalIntegration() {
        final List<MailRequest> requests = mapper.mapCreated(shipment(ShipmentStatus.CREATED,
                party("Jan", "jan@example.com", "PL"), party("Anna", "anna@example.com", "PL"), null, "EXTERNAL"));

        assertEquals(2, requests.size());
        assertEquals("shipment.created.external.sender", requests.get(0).templateKey());
        assertEquals("shipment.created.external.recipient", requests.get(1).templateKey());
    }

    @Test
    void shouldNotNotifyAboutCreatedReturnShipment() {
        final List<MailRequest> requests = mapper.mapCreated(shipment(ShipmentStatus.CREATED,
                party("Jan", "jan@example.com", "PL"), party("Anna", "anna@example.com", "PL"), new ShipmentId(7L)));

        assertTrue(requests.isEmpty());
    }

    @Test
    void shouldCreateRerouteRequestsWithNewPartyData() {
        final ShipmentRerouteRequestedIntegrationEvent event = new ShipmentRerouteRequestedIntegrationEvent(5L,
                new ShipmentId(100L), "PL1",
                new ReroutePartyData("Jan", "Nowak", "jan@example.com", "500", "Kraków", "30-001", "Rynek 1", "PL"),
                new ReroutePartyData("Anna", "Kowalska", "anna@example.com", "600", "Wien", "1010", "Ring 1", "AT"),
                Instant.parse("2026-10-18T10:00:00Z"));

        final List<MailRequest> requests = mapper.mapRerouteRequested(event);

        assertEquals(2, requests.size());
        assertEquals("shipment.reroute.requested.sender", requests.get(0).templateKey());
        assertEquals("shipment.reroute.requested.recipient", requests.get(1).templateKey());
        assertEquals(GERMAN, requests.get(1).locale());
        assertEquals("Wien", requests.get(0).variables().get("recipientCity"));
        assertEquals("Rynek 1", requests.get(1).variables().get("senderStreet"));
    }
}
