package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.*;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.DangerousGood;
import com.warehouse.shipment.domain.model.Signature;

public record ShipmentSnapshot(ShipmentId shipmentId,
                               Sender sender,
                               Recipient recipient,
                               ShipmentSize shipmentSize,
                               String destination,
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
                               Signature signature) {
}
