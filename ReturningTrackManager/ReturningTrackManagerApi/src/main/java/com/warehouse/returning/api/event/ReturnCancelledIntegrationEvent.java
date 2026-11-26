package com.warehouse.returning.api.event;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.returning.api.identificator.DepartmentId;
import com.warehouse.returning.api.identificator.OperatorId;
import com.warehouse.returning.api.identificator.ShipmentId;
import com.warehouse.returning.api.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

import java.time.Instant;
import java.util.UUID;

@IntegrationEventType(value = "return.cancelled", topic = ReturnTopics.CANCELLED)
public record ReturnCancelledIntegrationEvent(
        UUID eventId,
        ShipmentId shipmentId,
        ShipmentId returnShipmentId,
        ReturnPackageId returnPackageId,
        DepartmentId departmentId,
        OperatorId operatorId,
        UserId userId,
        Instant occurredAt) implements ReturnLifecycleIntegrationEvent {

    @Override
    public String eventKey() {
        return String.valueOf(shipmentId.value());
    }
}
