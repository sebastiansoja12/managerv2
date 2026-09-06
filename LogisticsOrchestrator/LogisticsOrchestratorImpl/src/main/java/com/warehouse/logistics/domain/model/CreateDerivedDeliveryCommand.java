package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.enumeration.DeliveryDerivationType;

public record CreateDerivedDeliveryCommand(ShipmentId shipmentId,
                                           ShipmentId sourceShipmentId,
                                           DeliveryDerivationType derivationType,
                                           Boolean signatureRequired,
                                           UserId userId) {
}
