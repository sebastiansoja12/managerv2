package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

public record ReassignDeliveryStepCarrierCommand(DeliveryId deliveryId,
                                                 int stepNumber,
                                                 SupplierId supplierId,
                                                 VehicleId vehicleId) {
}
