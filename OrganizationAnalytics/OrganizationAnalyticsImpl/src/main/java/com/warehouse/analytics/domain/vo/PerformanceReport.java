package com.warehouse.analytics.domain.vo;

import com.warehouse.commonassets.enumeration.Currency;

import java.util.List;
import java.util.Optional;

public record PerformanceReport(AnalyticsPeriod period,
                                Currency currency,
                                List<Currency> availableCurrencies,
                                DepartmentPerformance organization,
                                Optional<DepartmentPerformance> unassigned,
                                List<DepartmentPerformance> departments) {
}
