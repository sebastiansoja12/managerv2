package com.warehouse.analytics.domain.vo;

import com.warehouse.analytics.domain.exception.AnalyticsException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record AnalyticsPeriod(LocalDate dateFrom, LocalDate dateTo) {

    public static final long MAX_DAYS = 366;

    public AnalyticsPeriod {
        if (dateFrom == null || dateTo == null) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Analytics period requires both start and end date");
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Analytics period start date must not be after end date");
        }
        if (ChronoUnit.DAYS.between(dateFrom, dateTo) + 1 > MAX_DAYS) {
            throw new AnalyticsException(AnalyticsException.Reason.INVALID_REQUEST,
                    "Analytics period must not exceed " + MAX_DAYS + " days");
        }
    }

    public boolean contains(final LocalDate date) {
        return !date.isBefore(dateFrom) && !date.isAfter(dateTo);
    }
}
