package com.warehouse.returning.api.event;

import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

import java.time.Instant;
import java.util.UUID;

@IntegrationEventType(value = "return.cancelled", version = 1)
public record ReturnCancelledIntegrationEvent(
        UUID eventId,
        ShipmentId shipmentId,
        ReturnPackageId returnPackageId,
        DepartmentId departmentId,
        OperatorId operatorId,
        UserId userId,
        Instant occurredAt) implements ReturnLifecycleIntegrationEvent {

    @Override
    public String eventKey() {
        return String.valueOf(shipmentId.getValue());
    }
}
