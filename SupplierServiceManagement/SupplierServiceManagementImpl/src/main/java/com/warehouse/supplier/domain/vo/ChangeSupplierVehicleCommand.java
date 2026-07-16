package com.warehouse.supplier.domain.vo;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;

public record ChangeSupplierVehicleCommand(SupplierCode supplierCode, VehicleId vehicleId) {
}
