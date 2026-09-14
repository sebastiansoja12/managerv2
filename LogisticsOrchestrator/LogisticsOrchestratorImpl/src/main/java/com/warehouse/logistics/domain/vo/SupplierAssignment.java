package com.warehouse.logistics.domain.vo;

import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.VehicleId;

public record SupplierAssignment(SupplierId supplierId, VehicleId vehicleId) {
}
