package com.warehouse.shipment;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import com.warehouse.shipment.api.event.ShipmentChangedIntegrationEvent;
import com.warehouse.shipment.api.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.shipment.api.event.snapshot.ShipmentDeliveryData;
import com.warehouse.shipment.api.event.snapshot.ShipmentEventData;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryPlan;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentEventDataMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentChangedIntegrationEventTest {

    @Test
    void shouldDeserializeCreatedEventThroughSharedContract() throws Exception {
        final ShipmentEventData snapshot = new ShipmentEventDataMapper().map(DataTestCreator.shipment().snapshot());
        final ShipmentDeliveryData deliveryData = new ShipmentEventDataMapper().map(new ShipmentDeliveryPlan(
                new DepartmentId(9L), new DepartmentId(10L), PickupMethod.DEPARTMENT, DeliveryMethod.COURIER,
                null, null));
        final ShipmentCreatedIntegrationEvent original = new ShipmentCreatedIntegrationEvent(snapshot, deliveryData);
        final ObjectMapper objectMapper = JsonMapper.builder().configureForJackson2().build();

        final ShipmentCreatedIntegrationEvent restored = objectMapper.readValue(
                objectMapper.writeValueAsString(original), ShipmentCreatedIntegrationEvent.class);

        assertThat(restored).isInstanceOf(ShipmentChangedIntegrationEvent.class);
        assertThat(restored.payload().shipmentId()).isEqualTo(snapshot.shipmentId());
        assertThat(restored.delivery()).isEqualTo(deliveryData);
    }

    @Test
    void shouldSerializeLocalShipmentSnapshotToJson() throws Exception {
        final ShipmentEventData snapshot = new ShipmentEventDataMapper().map(DataTestCreator.shipment().snapshot());
        final ShipmentChangedIntegrationEvent event = new ShipmentChangedIntegrationEvent(
                snapshot
        );

        final ObjectMapper objectMapper = JsonMapper.builder().configureForJackson2().build();
        final JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(event));

        final JsonNode payload = json.path("payload");
        assertThat(payload.size()).isEqualTo(16);
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
        assertThat(json.has("userId")).isTrue();
        assertThat(json.has("departmentId")).isTrue();
        assertThat(json.has("operatorId")).isTrue();
    }
}
