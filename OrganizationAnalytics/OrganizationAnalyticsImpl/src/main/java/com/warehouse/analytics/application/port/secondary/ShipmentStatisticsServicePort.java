package com.warehouse.analytics.application.port.secondary;

import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.DailyRegistrations;
import com.warehouse.analytics.domain.vo.ShipmentFigures;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ShipmentStatisticsServicePort {

    ShipmentFigures shipmentFigures(final AnalyticsPeriod period);

    List<DailyRegistrations> dailyRegistrations(final LocalDate dateFrom, final LocalDate dateTo);

    Optional<LocalDate> firstRegistrationDate();
}
