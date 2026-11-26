package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;

import java.time.LocalDate;

public record DailyRegistrations(DepartmentId originDepartmentId, LocalDate day, long registeredShipments) {
}
