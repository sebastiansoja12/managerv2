package com.warehouse.analytics.application.port.primary.command;

import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Optional;

public record FindDepartmentCostsCommand(AnalyticsPeriod period, Optional<DepartmentCode> departmentCode) {
}
