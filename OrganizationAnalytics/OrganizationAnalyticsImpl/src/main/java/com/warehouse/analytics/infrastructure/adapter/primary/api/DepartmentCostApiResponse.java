package com.warehouse.analytics.infrastructure.adapter.primary.api;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record DepartmentCostApiResponse(DepartmentCostIdDto costId,
                                        String departmentCode,
                                        DepartmentCostCategory category,
                                        BigDecimal amount,
                                        Currency currency,
                                        LocalDate incurredOn,
                                        String description,
                                        UserIdDto recordedBy,
                                        Instant recordedAt) {
}
