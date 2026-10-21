package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentSnapshot(ShipmentId shipmentId,
                               Party sender,
                               Party recipient,
                               ShipmentStatus shipmentStatus,
                               ShipmentType shipmentType,
                               ShipmentId shipmentRelatedId,
                               Money price,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               Boolean locked,
                               Boolean signatureRequired,
                               ShipmentPriority shipmentPriority,
                               TrackingNumber trackingNumber,
                               ExternalId<UUID> externalShipmentId,
                               LocalDateTime acceptedAt,
                               LocalDateTime cancelledAt,
                               CancellationReason cancellationReason,
                               Dimensions dimensions,
                               Weight weight,
                               CustomerReference customerReference,
                               String contentDescription,
                               Money declaredValue,
                               ShipmentServiceLevel serviceLevel,
                               PackagingType packagingType) {
}
