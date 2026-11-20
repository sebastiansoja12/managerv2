package com.warehouse.analytics.domain.service;

import com.warehouse.analytics.domain.enumeration.DepartmentCostCategory;
import com.warehouse.analytics.domain.exception.AnalyticsException;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;
import com.warehouse.commonassets.identificator.UserId;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;

public class DepartmentCostFactory {

    private static final int DESCRIPTION_MAX_LENGTH = 500;

    private static final int AMOUNT_SCALE = 2;

    private final Clock clock;

    public DepartmentCostFactory(final Clock clock) {
        this.clock = clock;
    }

    public DepartmentCost create(final DepartmentCode departmentCode,
                                 final DepartmentCostCategory category,
                                 final BigDecimal amount,
                                 final Currency currency,
                                 final LocalDate incurredOn,
                                 final String description,
                                 final UserId recordedBy) {
        if (amount == null || amount.signum() <= 0) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Department cost amount must be greater than zero");
        }
        if (amount.stripTrailingZeros().scale() > AMOUNT_SCALE) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Department cost amount must have at most " + AMOUNT_SCALE + " decimal places");
        }
        if (incurredOn != null && incurredOn.isAfter(LocalDate.now(this.clock))) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Department cost cannot be recorded for a future date");
        }
        final String normalizedDescription = normalize(description);
        if (normalizedDescription != null && normalizedDescription.length() > DESCRIPTION_MAX_LENGTH) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Department cost description must not exceed " + DESCRIPTION_MAX_LENGTH + " characters");
        }
        return new DepartmentCost(DepartmentCostId.nextId(), departmentCode, category,
                amount.setScale(AMOUNT_SCALE), currency, incurredOn, normalizedDescription, recordedBy,
                this.clock.instant());
    }

    private String normalize(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
