package com.warehouse.logisticsdelivery.api.dto;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

public record ShipmentDeliverySupplierAssignmentRequestDto(ShipmentId shipmentId, SupplierId supplierId,
                                                           VehicleId vehicleId) {
}
