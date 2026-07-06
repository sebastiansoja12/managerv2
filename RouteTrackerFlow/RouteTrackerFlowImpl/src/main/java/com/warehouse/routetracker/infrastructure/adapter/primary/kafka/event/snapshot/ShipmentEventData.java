package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.routetracker.domain.vo.identifier.DepartmentId;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentEventData(
        ShipmentId shipmentId,
        SenderSnapshot sender,
        RecipientSnapshot recipient,
        ShipmentSize shipmentSize,
        DepartmentId targetDepartmentId,
        DepartmentId originDepartmentId,
        ShipmentStatus shipmentStatus,
        ShipmentType shipmentType,
        ShipmentId shipmentRelatedId,
        MoneySnapshot price,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime updatedAt,
        Boolean locked,
        DangerousGoodSnapshot dangerousGood,
        Boolean signatureRequired,
        ShipmentPriority shipmentPriority,
        CountryCode originCountry,
        CountryCode destinationCountry,
        SignatureSnapshot signature,
        TrackingNumber trackingNumber,
        PickupMethod pickupMethod,
        DeliveryMethod deliveryMethod,
        PickupPointId pickupPointId,
        ExternalId<UUID> externalShipmentId
) {
}
