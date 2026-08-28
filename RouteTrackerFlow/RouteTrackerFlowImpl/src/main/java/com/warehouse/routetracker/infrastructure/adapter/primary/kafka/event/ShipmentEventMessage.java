package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event;

import java.time.Instant;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot.ShipmentSnapshot;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentEventMessage(
        UUID eventId,
        String eventType,
        int version,
        Instant occurredAt,
        ShipmentSnapshot payload,
        UserId userId,
        DepartmentId departmentId,
        OperatorId operatorId
) {
}
