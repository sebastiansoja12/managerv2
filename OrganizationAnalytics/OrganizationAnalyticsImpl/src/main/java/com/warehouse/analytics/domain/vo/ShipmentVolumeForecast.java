package com.warehouse.analytics.domain.vo;

import com.warehouse.analytics.domain.enumeration.ForecastStatus;

import java.time.LocalDate;
import java.util.List;

public record ShipmentVolumeForecast(LocalDate targetDate,
                                     ForecastStatus status,
                                     List<LocalDate> observationDays,
                                     int requiredObservations,
                                     DepartmentForecast organization,
                                     List<DepartmentForecast> departments) {
}
