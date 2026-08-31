package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import com.warehouse.routetracker.domain.enumeration.ShipmentStatus;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;
import com.warehouse.routetracker.domain.vo.identifier.OperatorId;
import com.warehouse.routetracker.domain.vo.identifier.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.api.ShipmentId;

import java.time.LocalDateTime;

public record ShipmentChangedEventPayload(
        ShipmentId shipmentId,
        String eventType,
        ShipmentStatus shipmentStatus,
        LocalDateTime changedAt,
        OperatorId operatorId,
        DepartmentId departmentId,
        UserId userId
) {
}
