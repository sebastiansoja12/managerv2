package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot;

class ShipmentChangedIntegrationEventTest {

    @Test
    void shouldSerializeLocalShipmentSnapshotToJson() throws Exception {
        final ShipmentChangedIntegrationEvent event = new ShipmentChangedIntegrationEvent(
                ShipmentSnapshot.from(DataTestCreator.shipment().snapshot())
        );

        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(event));

        final JsonNode payload = json.path("payload");
        assertThat(payload.size()).isEqualTo(21);
        assertThat(payload.path("shipmentId").path("value").asLong()).isEqualTo(1L);
        assertThat(payload.path("trackingNumber").path("value").asText())
                .isEqualTo("TEST-TRACKING-NUMBER");
        assertThat(payload.path("shipmentStatus").asText()).isEqualTo("CREATED");
        assertThat(payload.path("shipmentType").asText()).isEqualTo("PARENT");
        assertThat(payload.path("shipmentSize").asText()).isEqualTo("SMALL");
        assertThat(payload.path("sender").path("firstName").asText()).isEqualTo("updatedTest");
        assertThat(payload.path("recipient").path("email").asText()).isEqualTo("test@test.pl");
        assertThat(payload.path("destination").path("value").asText()).isEqualTo("KT1");
        assertThat(payload.path("price").path("amount").decimalValue()).isEqualByComparingTo("10");
        assertThat(payload.path("price").path("currency").asText()).isEqualTo("PLN");
        assertThat(payload.path("shipmentPriority").asText()).isEqualTo("MEDIUM");
        assertThat(payload.path("originCountry").asText()).isEqualTo("PL");
        assertThat(payload.path("destinationCountry").asText()).isEqualTo("DE");
        assertThat(payload.path("externalShipmentId").path("value").asText()).isNotEmpty();
        assertThat(json.has("eventId")).isFalse();
        assertThat(json.has("eventType")).isFalse();
        assertThat(json.has("eventVersion")).isFalse();
        assertThat(json.has("occurredAt")).isFalse();
        assertThat(json.has("userId")).isFalse();
        assertThat(json.has("departmentId")).isFalse();
        assertThat(json.has("operatorId")).isFalse();
    }
}
