package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ExternalId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Signature;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShipmentSnapshot(ShipmentId shipmentId,
                               Sender sender,
                               Recipient recipient,
                               ShipmentSize shipmentSize,
                               DepartmentCode destination,
                               DepartmentId originDepartmentId,
                               ShipmentStatus shipmentStatus,
                               ShipmentType shipmentType,
                               ShipmentId shipmentRelatedId,
                               Money price,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt,
                               Boolean locked,
                               DangerousGood dangerousGood,
                               Boolean signatureRequired,
                               ShipmentPriority shipmentPriority,
                               CountryCode originCountry,
                               CountryCode destinationCountry,
                               Signature signature,
                               TrackingNumber trackingNumber,
                               ExternalId<UUID> externalShipmentId) {
}
