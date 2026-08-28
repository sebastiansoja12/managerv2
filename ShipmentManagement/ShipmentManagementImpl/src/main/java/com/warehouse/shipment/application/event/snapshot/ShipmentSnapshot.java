package com.warehouse.shipment.application.event.snapshot;

import java.time.LocalDateTime;
import java.util.UUID;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;

public record ShipmentSnapshot(
        ShipmentId shipmentId,
        SenderSnapshot sender,
        RecipientSnapshot recipient,
        ShipmentSize shipmentSize,
        DepartmentCode destination,
        DepartmentId originDepartmentId,
        ShipmentStatus shipmentStatus,
        ShipmentType shipmentType,
        ShipmentId shipmentRelatedId,
        MoneySnapshot price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Boolean locked,
        DangerousGoodSnapshot dangerousGood,
        Boolean signatureRequired,
        ShipmentPriority shipmentPriority,
        CountryCode originCountry,
        CountryCode destinationCountry,
        SignatureSnapshot signature,
        TrackingNumber trackingNumber,
        ExternalId<UUID> externalShipmentId
) {

    public static ShipmentSnapshot from(final com.warehouse.shipment.domain.vo.ShipmentSnapshot snapshot) {
        return new ShipmentSnapshot(
                snapshot.shipmentId(),
                SenderSnapshot.from(snapshot.sender()),
                RecipientSnapshot.from(snapshot.recipient()),
                snapshot.shipmentSize(),
                snapshot.destination(),
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
                snapshot.externalShipmentId()
        );
    }
}
