package com.warehouse.analytics.infrastructure.adapter.primary.api;

import com.warehouse.commonassets.enumeration.Currency;

import java.time.LocalDate;
import java.util.List;

public record PerformanceReportApiResponse(LocalDate dateFrom,
                                           LocalDate dateTo,
                                           Currency currency,
                                           List<Currency> availableCurrencies,
                                           DepartmentPerformanceApiResponse organization,
                                           DepartmentPerformanceApiResponse unassigned,
                                           List<DepartmentPerformanceApiResponse> departments) {
}
