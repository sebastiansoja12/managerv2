package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class ShipmentChangedIntegrationEventTest {

    @Test
    void shouldSerializeLocalShipmentSnapshotToJson() throws Exception {
        final ShipmentEventData snapshot = ShipmentEventData.from(DataTestCreator.shipment().snapshot());
        final ShipmentChangedIntegrationEvent event = new ShipmentChangedIntegrationEvent(
                snapshot
        );

        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(event));

        final JsonNode payload = json.path("payload");
        assertThat(payload.size()).isEqualTo(7);
        assertThat(payload.path("shipmentId").path("value").asLong()).isEqualTo(1L);
        assertThat(payload.path("eventType").asText()).isEqualTo("shipment.changed");
        assertThat(payload.path("shipmentStatus").asText()).isEqualTo("CREATED");
        assertThat(payload.path("changedAt").asText()).isEqualTo(snapshot.updatedAt().toString());
        assertThat(payload.path("operatorId").isNull()).isTrue();
        assertThat(payload.path("departmentId").isNull()).isTrue();
        assertThat(payload.path("userId").isNull()).isTrue();
        assertThat(json.has("eventId")).isFalse();
        assertThat(json.has("eventType")).isFalse();
        assertThat(json.has("eventVersion")).isFalse();
        assertThat(json.has("occurredAt")).isFalse();
        assertThat(json.has("userId")).isFalse();
        assertThat(json.has("departmentId")).isFalse();
        assertThat(json.has("operatorId")).isFalse();
    }
}
