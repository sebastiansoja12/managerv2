package com.warehouse.supplier.application.port.primary.command;

import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;

public record ChangeSupplierVehicleCommand(SupplierCode supplierCode, VehicleId vehicleId) {
}
