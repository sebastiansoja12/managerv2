package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.CreateShipmentEventCommand;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

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
    public void handle(final ShipmentEventMessage message) {
        final CreateShipmentEventCommand command = new CreateShipmentEventCommand(
                message.eventId(),
                new ShipmentId(message.payload().shipmentId().getValue()),
                message.eventType(),
                ShipmentStatus.valueOf(message.payload().shipmentStatus().name()),
                LocalDateTime.ofInstant(message.occurredAt(), ZoneOffset.UTC),
                this.serialize(message),
                message.userId(),
                message.departmentId()
        );
        this.routeTrackerLogPort.createShipmentEvent(command);
        log.info("Processed shipment event {} for {}", message.eventType(),
                message.payload().shipmentId().getValue());
    }

    private String serialize(final ShipmentEventMessage event) {
        try {
            return this.objectMapper.writeValueAsString(event);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot serialize shipment event", exception);
        }
    }
}
