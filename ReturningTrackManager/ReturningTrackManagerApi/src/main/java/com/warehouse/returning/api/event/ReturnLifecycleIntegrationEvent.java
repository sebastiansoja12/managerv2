package com.warehouse.returning.api.event;

import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

import java.time.Instant;
import java.util.UUID;

public interface ReturnLifecycleIntegrationEvent extends IntegrationEvent, IntegrationEventKey {
    UUID eventId();
    ShipmentId shipmentId();
    ReturnPackageId returnPackageId();
    DepartmentId departmentId();
    OperatorId operatorId();
    UserId userId();
    Instant occurredAt();
}
