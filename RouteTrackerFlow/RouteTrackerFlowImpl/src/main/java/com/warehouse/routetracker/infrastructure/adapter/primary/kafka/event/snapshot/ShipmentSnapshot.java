package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonIgnoreProperties(ignoreUnknown = true)
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
}
