package com.warehouse.analytics.domain.model;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import com.warehouse.commonassets.identificator.UserId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class DepartmentCost {

    private final DepartmentCostId costId;

    private final DepartmentCode departmentCode;

    private final DepartmentCostCategory category;

    private final BigDecimal amount;

    private final Currency currency;

    private final LocalDate incurredOn;

    private final String description;

    private final UserId recordedBy;

    private final Instant recordedAt;

    public DepartmentCost(final DepartmentCostId costId,
                           final DepartmentCode departmentCode,
                           final DepartmentCostCategory category,
                           final BigDecimal amount,
                           final Currency currency,
                           final LocalDate incurredOn,
                           final String description,
                           final UserId recordedBy,
                           final Instant recordedAt) {
        this.costId = Objects.requireNonNull(costId, "Department cost ID cannot be null");
        this.departmentCode = Objects.requireNonNull(departmentCode, "Department code cannot be null");
        this.category = Objects.requireNonNull(category, "Department cost category cannot be null");
        this.amount = Objects.requireNonNull(amount, "Department cost amount cannot be null");
        this.currency = Objects.requireNonNull(currency, "Department cost currency cannot be null");
        this.incurredOn = Objects.requireNonNull(incurredOn, "Department cost date cannot be null");
        this.description = description;
        this.recordedBy = Objects.requireNonNull(recordedBy, "Department cost author cannot be null");
        this.recordedAt = Objects.requireNonNull(recordedAt, "Department cost record time cannot be null");
    }

    public DepartmentCostId costId() {
        return costId;
    }

    public DepartmentCode departmentCode() {
        return departmentCode;
    }

    public DepartmentCostCategory category() {
        return category;
    }

    public BigDecimal amount() {
        return amount;
    }

    public Currency currency() {
        return currency;
    }

    public LocalDate incurredOn() {
        return incurredOn;
    }

    public String description() {
        return description;
    }

    public UserId recordedBy() {
        return recordedBy;
    }

    public Instant recordedAt() {
        return recordedAt;
    }
}
