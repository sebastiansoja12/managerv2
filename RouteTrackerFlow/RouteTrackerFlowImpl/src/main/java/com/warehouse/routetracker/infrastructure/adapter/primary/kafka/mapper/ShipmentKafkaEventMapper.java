package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.mapper;

import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedIntegrationEvent;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentEventData;
import org.springframework.stereotype.Component;

@Component
public class ShipmentKafkaEventMapper {

    public ShipmentStatusStateChangeCommand map(final ShipmentChangedIntegrationEvent event) {
        final ShipmentEventData payload = event.payload();
        return new ShipmentStatusStateChangeCommand(
                new ShipmentId(payload.shipmentId().value()),
                event.eventType(),
                com.warehouse.routetracker.domain.enumeration.ShipmentStatus.valueOf(
                        payload.shipmentStatus().name()),
                payload.updatedAt(),
                event.operatorId(),
                event.departmentId(),
                event.userId()
        );
    }
}
