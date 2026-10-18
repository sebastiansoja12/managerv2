package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.math.BigDecimal;

public record ShipmentDetailsDto(ShipmentId shipmentId,
                                 ShipmentStatus status,
                                 ShipmentId relatedShipmentId,
                                 BigDecimal weightInKilograms,
                                 String trackingNumber,
                                 String recipientCity,
                                 String recipientPostalCode) {

    public ShipmentDetailsDto(final ShipmentId shipmentId,
                              final ShipmentStatus status,
                              final ShipmentId relatedShipmentId,
                              final BigDecimal weightInKilograms) {
        this(shipmentId, status, relatedShipmentId, weightInKilograms, null, null, null);
    }
}
