package com.warehouse.shipment.infrastructure.dto;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.LocalDate;

public record DailyShipmentCountDto(DepartmentId originDepartmentId, LocalDate day, long registeredShipments) {
}
