package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventHeaders;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChanged;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentCreated;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentReturned;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@KafkaListener(
        topics = "${manager.kafka.topics.shipment-events:shipment.events}",
        groupId = "${spring.kafka.consumer.group-id:route-tracker-flow}"
)
public class ShipmentKafkaListener {

    private final ObjectMapper objectMapper;
    private final RouteTrackerLogPort routeTrackerLogPort;

    public ShipmentKafkaListener(final ObjectMapper objectMapper, final RouteTrackerLogPort routeTrackerLogPort) {
        this.objectMapper = objectMapper;
        this.routeTrackerLogPort = routeTrackerLogPort;
    }

    @KafkaHandler
    public void handle(final ShipmentCreated event) {
        this.saveShipmentEvent(event, event.getClass().getSimpleName(), ShipmentStatus.CREATED);
    }

    @KafkaHandler
    public void handle(final ShipmentReturned event) {
        this.saveShipmentEvent(event, event.getClass().getSimpleName(), ShipmentStatus.RETURN);
    }

    @KafkaHandler
    public void handle(final ShipmentChanged event,
                       @Header(KafkaEventHeaders.EVENT_TYPE) final byte[] eventType) {
        this.saveShipmentEvent(
                event,
                this.headerValue(eventType),
                ShipmentStatus.valueOf(event.shipmentStatus()));
    }

    private void saveShipmentEvent(final ShipmentEvent event,
                                   final String eventType,
                                   final ShipmentStatus shipmentStatus) {
        final LocalDateTime occurredAt = LocalDateTime.ofInstant(event.timestamp(), ZoneOffset.UTC);
        final String payload = this.serialize(event);

        this.routeTrackerLogPort.createShipmentEvent(
                new ShipmentId(event.shipmentId()),
                eventType,
                shipmentStatus,
                occurredAt,
                payload,
                event.userId(),
                event.departmentId());
        log.info("Processed shipment event {} for {}", eventType, event.shipmentId());
    }

    private String headerValue(final byte[] value) {
        return new String(value, StandardCharsets.UTF_8);
    }

    private String serialize(final ShipmentEvent event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize shipment event", exception);
        }
    }
}
