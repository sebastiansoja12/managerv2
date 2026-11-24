package com.warehouse.returning.api.event;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.returning.api.identificator.DepartmentId;
import com.warehouse.returning.api.identificator.OperatorId;
import com.warehouse.returning.api.identificator.ShipmentId;
import com.warehouse.returning.api.identificator.UserId;
import com.warehouse.returning.domain.vo.ReturnPackageId;

import java.time.Instant;
import java.util.UUID;

public interface ReturnLifecycleIntegrationEvent extends IntegrationEvent, IntegrationEventKey {
    UUID eventId();
    ShipmentId shipmentId();
    ShipmentId returnShipmentId();
    ReturnPackageId returnPackageId();
    DepartmentId departmentId();
    OperatorId operatorId();
    UserId userId();
    Instant occurredAt();
}
