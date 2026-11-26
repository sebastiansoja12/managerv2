package com.warehouse.logisticsdelivery.domain.vo;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.AssignmentWindowType;
import com.warehouse.logisticsdelivery.domain.enumeration.CourierAssignmentDateField;

import java.time.Duration;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Objects;

public record DepartmentAssignmentConfig(DepartmentId departmentId,
                                         boolean enabled,
                                         CourierAssignmentDateField dateField,
                                         AssignmentWindowType windowType,
                                         LocalTime windowStart,
                                         LocalTime windowEnd,
                                         ZoneId timeZone,
                                         Duration processingDelay,
                                         Duration overlap,
                                         Duration maxWindow,
                                         Duration lookback,
                                         int batchSize) {

    public DepartmentAssignmentConfig {
        Objects.requireNonNull(departmentId, "Department id must not be null");
        Objects.requireNonNull(dateField, "Date field must not be null");
        Objects.requireNonNull(windowType, "Window type must not be null");
        Objects.requireNonNull(windowStart, "Window start must not be null");
        Objects.requireNonNull(windowEnd, "Window end must not be null");
        Objects.requireNonNull(timeZone, "Time zone must not be null");
        if (processingDelay.isNegative() || overlap.isNegative()) {
            throw new IllegalArgumentException("Processing delay and overlap must not be negative");
        }
        if (!maxWindow.isPositive() || !lookback.isPositive() || batchSize < 1) {
            throw new IllegalArgumentException("Max window, lookback and batch size must be positive");
        }
    }
}
