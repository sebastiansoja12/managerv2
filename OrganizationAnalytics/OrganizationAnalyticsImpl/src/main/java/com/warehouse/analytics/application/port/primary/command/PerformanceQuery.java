package com.warehouse.analytics.application.port.primary.command;

import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;

import java.util.Set;

public record PerformanceQuery(AnalyticsPeriod period, Set<DepartmentCode> departmentCodes, Currency currency) {
}
