package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.math.BigDecimal;

public record ShipmentDocumentDto(ShipmentId shipmentId,
                                  ShipmentId relatedShipmentId,
                                  ShipmentStatus status,
                                  String trackingNumber,
                                  BigDecimal weightInKilograms,
                                  ShipmentPartyDto sender,
                                  ShipmentPartyDto recipient) {
}
