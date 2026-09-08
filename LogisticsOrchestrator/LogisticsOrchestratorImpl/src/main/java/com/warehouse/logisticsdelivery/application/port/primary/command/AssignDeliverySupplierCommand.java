package com.warehouse.logisticsdelivery.application.port.primary.command;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

public record AssignDeliverySupplierCommand(DeliveryId deliveryId, SupplierId supplierId, VehicleId vehicleId) {
}
