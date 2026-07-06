package com.warehouse.shipment.application.event.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.LocalDateTime;
import java.util.UUID;

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
        PickupPointId deliveryPickupPointId,
        ExternalId<UUID> externalShipmentId
) {

    public static ShipmentEventData from(final ShipmentSnapshot snapshot) {
        return new ShipmentEventData(
                snapshot.shipmentId(),
                SenderSnapshot.from(snapshot.sender()),
                RecipientSnapshot.from(snapshot.recipient()),
                snapshot.shipmentSize(),
                snapshot.targetDepartmentId(),
                snapshot.originDepartmentId(),
                snapshot.shipmentStatus(),
                snapshot.shipmentType(),
                snapshot.shipmentRelatedId(),
                MoneySnapshot.from(snapshot.price()),
                snapshot.createdAt(),
                snapshot.updatedAt(),
                snapshot.locked(),
                DangerousGoodSnapshot.from(snapshot.dangerousGood()),
                snapshot.signatureRequired(),
                snapshot.shipmentPriority(),
                snapshot.originCountry(),
                snapshot.destinationCountry(),
                SignatureSnapshot.from(snapshot.signature()),
                snapshot.trackingNumber(),
                snapshot.pickupMethod(),
                snapshot.deliveryMethod(),
                snapshot.pickupPointId(),
                snapshot.deliveryPickupPointId(),
                snapshot.externalShipmentId()
        );
    }
}
