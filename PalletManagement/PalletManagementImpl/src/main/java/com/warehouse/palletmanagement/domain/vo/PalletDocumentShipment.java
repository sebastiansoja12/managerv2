package com.warehouse.palletmanagement.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

import java.math.BigDecimal;

public record PalletDocumentShipment(ShipmentId shipmentId,
                                     String trackingNumber,
                                     ShipmentStatus status,
                                     String recipientCity,
                                     String recipientPostalCode,
                                     BigDecimal weightInKilograms) {

    public static PalletDocumentShipment unknown(final ShipmentId shipmentId) {
        return new PalletDocumentShipment(shipmentId, null, null, null, null, null);
    }
}
