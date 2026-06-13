package com.warehouse.shipment;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.event.ShipmentCreatedIntegrationEvent;
import com.warehouse.shipment.application.event.snapshot.ShipmentSnapshot;

class ShipmentCreatedIntegrationEventTest {

    @Test
    void shouldSerializeLocalShipmentSnapshotToJson() throws Exception {
        final ShipmentCreatedIntegrationEvent event = new ShipmentCreatedIntegrationEvent(
                UUID.fromString("f783d37e-06e9-4efc-8f18-099343b150e8"),
                "shipment.created",
                1,
                Instant.parse("2026-08-11T10:15:30Z"),
                ShipmentSnapshot.from(DataTestCreator.shipment().snapshot())
        );
        event.assignOperatorContext(OperatorId.of(7L), new UserId(42L), new DepartmentId(10L));

        final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        final JsonNode json = objectMapper.readTree(objectMapper.writeValueAsString(event));

        assertThat(json.path("eventType").asText()).isEqualTo("shipment.created");
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
        assertThat(json.path("userId").path("value").asLong()).isEqualTo(42L);
        assertThat(json.path("userId").has("admin")).isFalse();
        assertThat(json.path("departmentId").path("value").asLong()).isEqualTo(10L);
        assertThat(json.path("operatorId").path("value").asLong()).isEqualTo(7L);
    }
}
