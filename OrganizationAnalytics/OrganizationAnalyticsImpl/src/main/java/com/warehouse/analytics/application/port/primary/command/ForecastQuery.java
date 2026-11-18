package com.warehouse.analytics.application.port.primary.command;

import com.warehouse.commonassets.identificator.DepartmentCode;

import java.time.LocalDate;
import java.util.Optional;

public record ForecastQuery(LocalDate targetDate, Optional<DepartmentCode> departmentCode) {
}
