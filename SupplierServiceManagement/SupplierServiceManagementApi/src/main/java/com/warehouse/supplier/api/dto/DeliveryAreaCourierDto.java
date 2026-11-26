package com.warehouse.supplier.api.dto;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

import java.util.Set;

public record DeliveryAreaCourierDto(SupplierId supplierId, VehicleId vehicleId, Set<String> postalCodes) {
}
