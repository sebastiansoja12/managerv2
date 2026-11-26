package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.LocalDate;

public record DailyShipmentCount(DepartmentId originDepartmentId, LocalDate day, long registeredShipments) {
}
