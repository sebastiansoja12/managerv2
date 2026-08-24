package com.warehouse.logistics.domain.model;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;

public record CreateDeliveryCommand(ShipmentId shipmentId,
                                    DeliveryMethod method,
                                    PickupPointId pickupPointId,
                                    PickupPointId deliveryPickupPointId,
                                    ShipmentId signatureId,
                                    Boolean signatureRequired,
                                    UserId userId) {
}
