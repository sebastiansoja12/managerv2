package com.warehouse.analytics.infrastructure.adapter.primary.api;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.commonassets.enumeration.Currency;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecordDepartmentCostApiRequest(@NotBlank @Size(max = 40) String departmentCode,
                                             @NotNull DepartmentCostCategory category,
                                             @NotNull @Positive @Digits(integer = 17, fraction = 2) BigDecimal amount,
                                             @NotNull Currency currency,
                                             @NotNull LocalDate incurredOn,
                                             @Size(max = 500) String description) {
}
