package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.mapper;

import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedEventPayload;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedIntegrationEvent;
import org.springframework.stereotype.Component;

@Component
public class ShipmentKafkaEventMapper {

    public ShipmentStatusStateChangeCommand map(final ShipmentChangedIntegrationEvent event) {
        final ShipmentChangedEventPayload payload = event.payload();
        return new ShipmentStatusStateChangeCommand(
                payload.shipmentId(),
                payload.eventType(),
                payload.shipmentStatus(),
                payload.changedAt(),
                payload.operatorId(),
                payload.departmentId(),
                payload.userId()
        );
    }
}
