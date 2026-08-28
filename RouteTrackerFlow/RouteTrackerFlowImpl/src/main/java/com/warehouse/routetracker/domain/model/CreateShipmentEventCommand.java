package com.warehouse.routetracker.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

public record CreateShipmentEventCommand(
        UUID eventId,
        ShipmentId shipmentId,
        String eventType,
        ShipmentStatus shipmentStatus,
        LocalDateTime occurredAt,
        String payload,
        UserId userId,
        DepartmentId departmentId
) {
}
