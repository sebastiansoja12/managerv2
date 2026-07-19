package com.warehouse.routetracker.infrastructure.adapter.primary.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.routetracker.domain.port.primary.RouteTrackerLogPort;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentCreatedRequest;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.UserId;

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
            topics = "${manager.kafka.topics.shipment-created:shipment.created}",
            groupId = "${spring.kafka.consumer.group-id:route-tracker-flow}"
    )
    public void handle(final String payload) {
        final ShipmentCreatedMessage message = this.deserialize(payload);
        final ShipmentCreatedRequest request = this.toShipmentCreatedRequest(message);

        this.routeTrackerLogPort.initializeRouteProcess(request.shipmentId());
        log.info("Processed shipment creation event for {}", message.shipmentId());
    }

    private ShipmentCreatedMessage deserialize(final String payload) {
        try {
            return this.objectMapper.readValue(payload, ShipmentCreatedMessage.class);
        } catch (final JsonProcessingException exception) {
            throw new IllegalStateException("Cannot deserialize shipment event", exception);
        }
    }

    private ShipmentCreatedRequest toShipmentCreatedRequest(final ShipmentCreatedMessage message) {
        return new ShipmentCreatedRequest(new com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId(
                message.shipmentId()), new UserId(message.createdBy()));
    }

    private record ShipmentCreatedMessage(Long shipmentId, Long createdBy) {
    }
}
