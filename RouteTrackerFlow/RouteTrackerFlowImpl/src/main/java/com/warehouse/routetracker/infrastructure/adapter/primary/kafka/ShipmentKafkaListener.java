package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.enumeration.ParcelStatus;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ShipmentKafkaListener {

    private final ObjectMapper objectMapper;
    private final RouteTrackerLogPort routeTrackerLogPort;

    public ShipmentKafkaListener(final ObjectMapper objectMapper, final RouteTrackerLogPort routeTrackerLogPort) {
        this.objectMapper = objectMapper;
        this.routeTrackerLogPort = routeTrackerLogPort;
    }

    @KafkaListener(
            topics = "${manager.kafka.topics.shipment-events:shipment.events}",
            groupId = "${spring.kafka.consumer.group-id:route-tracker-flow}"
    )
    public void handle(final String payload) {
        final ShipmentEventMessage message = this.deserialize(payload);
        final Long shipmentId = message.event().snapshot().shipmentId().value();
        final ParcelStatus parcelStatus = ParcelStatus.valueOf(message.event().snapshot().shipmentStatus());
        final LocalDateTime occurredAt = LocalDateTime.ofInstant(message.event().timestamp(), ZoneOffset.UTC);

        this.routeTrackerLogPort.saveShipmentEvent(
                new ShipmentId(shipmentId), message.eventType(), parcelStatus, occurredAt, payload);
        log.info("Processed shipment event {} for {}", message.eventType(), shipmentId);
    }

    private ShipmentEventMessage deserialize(final String payload) {
        try {
            return this.objectMapper.readValue(payload, ShipmentEventMessage.class);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot deserialize shipment event", exception);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ShipmentEventMessage(String eventType, ShipmentEventPayload event) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ShipmentEventPayload(ShipmentSnapshot snapshot, Instant timestamp) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ShipmentSnapshot(ShipmentIdPayload shipmentId, String shipmentStatus) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record ShipmentIdPayload(Long value) {
    }
}
