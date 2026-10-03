package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEventDataMapper;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

class ShipmentChangedIntegrationEventTest {

    @Test
    void shouldDeserializeCreatedEventThroughSharedContract() throws Exception {
        final ShipmentEventData snapshot = new ShipmentEventDataMapper().map(DataTestCreator.shipment().snapshot());
        final ShipmentCreatedIntegrationEvent original = new ShipmentCreatedIntegrationEvent(snapshot);
        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

        final ShipmentCreatedIntegrationEvent restored = objectMapper.readValue(
                objectMapper.writeValueAsString(original), ShipmentCreatedIntegrationEvent.class);

        assertThat(restored).isInstanceOf(ShipmentChangedIntegrationEvent.class);
        assertThat(restored.payload().shipmentId()).isEqualTo(snapshot.shipmentId());
        assertThat(restored.payload().deliveryMethod()).isEqualTo(snapshot.deliveryMethod());
    }

    @Test
    void shouldSerializeLocalShipmentSnapshotToJson() throws Exception {
        final ShipmentEventData snapshot = new ShipmentEventDataMapper().map(DataTestCreator.shipment().snapshot());
        final ShipmentChangedIntegrationEvent event = new ShipmentChangedIntegrationEvent(
                snapshot
        );

        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(event));

        final JsonNode payload = json.path("payload");
        assertThat(payload.size()).isEqualTo(22);
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
