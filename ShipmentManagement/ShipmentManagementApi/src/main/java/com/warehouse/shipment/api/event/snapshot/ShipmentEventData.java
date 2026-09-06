package com.warehouse.shipment.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record ShipmentEventData(
        ShipmentId shipmentId,
        PartySnapshot sender,
        PartySnapshot recipient,
        ShipmentStatus shipmentStatus,
        ShipmentType shipmentType,
        ShipmentId shipmentRelatedId,
        MoneySnapshot price,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING) LocalDateTime updatedAt,
        Boolean locked,
        Boolean signatureRequired,
        ShipmentPriority shipmentPriority,
        TrackingNumber trackingNumber,
        ExternalId<UUID> externalShipmentId,
        ShipmentServiceLevel serviceLevel,
        String packagingType
) {
    public enum ShipmentServiceLevel {
        ECONOMY,
        STANDARD,
        EXPRESS
    }
}
