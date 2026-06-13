package com.warehouse.shipment.application.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.event.snapshot.ShipmentEventData;

import java.time.LocalDateTime;

public record ShipmentChangedEventPayload(
        ShipmentId shipmentId,
        String eventType,
        ShipmentStatus shipmentStatus,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime changedAt,
        OperatorId operatorId,
        DepartmentId departmentId,
        UserId userId
) {

    public static ShipmentChangedEventPayload from(final ShipmentEventData shipment, final String eventType) {
        return new ShipmentChangedEventPayload(
                shipment.shipmentId(),
                eventType,
                shipment.shipmentStatus(),
                shipment.updatedAt(),
                null,
                null,
                null
        );
    }

    public ShipmentChangedEventPayload withOperatorContext(final OperatorId operatorId,
                                                           final DepartmentId departmentId,
                                                           final UserId userId) {
        return new ShipmentChangedEventPayload(
                shipmentId,
                eventType,
                shipmentStatus,
                changedAt,
                operatorId,
                departmentId,
                userId
        );
    }
}
