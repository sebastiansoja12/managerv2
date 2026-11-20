package com.warehouse.analytics.infrastructure.adapter.primary.api;

import com.warehouse.analytics.domain.enumeration.ForecastStatus;

import java.time.LocalDate;
import java.util.List;

public record ShipmentForecastApiResponse(LocalDate targetDate,
                                          ForecastStatus status,
                                          List<LocalDate> observationDays,
                                          int requiredObservations,
                                          DepartmentForecastApiResponse organization,
                                          List<DepartmentForecastApiResponse> departments) {
}
