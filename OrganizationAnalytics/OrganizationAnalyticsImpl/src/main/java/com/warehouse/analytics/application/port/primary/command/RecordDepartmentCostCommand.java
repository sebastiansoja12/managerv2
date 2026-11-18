package com.warehouse.analytics.application.port.primary.command;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecordDepartmentCostCommand(DepartmentCode departmentCode,
                                          DepartmentCostCategory category,
                                          BigDecimal amount,
                                          Currency currency,
                                          LocalDate incurredOn,
                                          String description) {
}
