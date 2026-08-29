package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.mapper;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.model.ShipmentStatusStateChangeCommand;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.ShipmentChangedIntegrationEvent;
import org.springframework.stereotype.Component;

@Component
public class ShipmentKafkaEventMapper {

    public ShipmentStatusStateChangeCommand map(final ShipmentChangedIntegrationEvent event) {
        return new ShipmentStatusStateChangeCommand(
                new ShipmentId(event.payload().shipmentId().getValue()),
                ShipmentChangedIntegrationEvent.TYPE,
                ShipmentStatus.valueOf(event.payload().shipmentStatus().name()),
                event.payload().updatedAt(),
                event.operatorId(),
                event.departmentId(),
                event.userId()
        );
    }
}
