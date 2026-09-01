package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.shipment.application.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

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
        assertThat(payload.size()).isEqualTo(21);
        assertThat(payload.path("shipmentId").path("value").asLong()).isEqualTo(1L);
        assertThat(payload.path("shipmentStatus").asText()).isEqualTo("CREATED");
        assertThat(LocalDateTime.parse(payload.path("updatedAt").asText())).isEqualTo(snapshot.updatedAt());
        assertThat(payload.has("eventType")).isFalse();
        assertThat(payload.has("operatorId")).isFalse();
        assertThat(payload.has("departmentId")).isFalse();
        assertThat(payload.has("userId")).isFalse();
        assertThat(json.has("eventId")).isFalse();
        assertThat(json.has("eventType")).isFalse();
        assertThat(json.has("eventVersion")).isFalse();
        assertThat(json.has("occurredAt")).isFalse();
        assertThat(json.has("userId")).isFalse();
        assertThat(json.has("departmentId")).isFalse();
        assertThat(json.has("operatorId")).isFalse();
    }
}
