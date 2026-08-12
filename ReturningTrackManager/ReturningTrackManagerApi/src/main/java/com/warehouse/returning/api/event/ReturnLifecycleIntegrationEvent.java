package com.warehouse.returning.api.event;

import com.warehouse.returning.api.identificator.DepartmentId;
import com.warehouse.returning.api.identificator.OperatorId;
import com.warehouse.returning.api.identificator.ShipmentId;
import com.warehouse.returning.api.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

import java.time.Instant;
import java.util.UUID;

public interface ReturnLifecycleIntegrationEvent {
    UUID eventId();
    ShipmentId shipmentId();
    ReturnPackageId returnPackageId();
    DepartmentId departmentId();
    OperatorId operatorId();
    UserId userId();
    Instant occurredAt();

    String eventType();

    int version();

    String eventKey();
}
