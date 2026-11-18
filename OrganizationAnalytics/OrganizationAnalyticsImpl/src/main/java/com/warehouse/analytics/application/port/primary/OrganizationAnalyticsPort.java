package com.warehouse.analytics.application.port.primary;

import com.warehouse.analytics.application.port.primary.command.ForecastQuery;
import com.warehouse.analytics.application.port.primary.command.PerformanceQuery;
import com.warehouse.analytics.domain.vo.PerformanceReport;
import com.warehouse.analytics.domain.vo.ShipmentVolumeForecast;

public interface OrganizationAnalyticsPort {

    PerformanceReport performance(final PerformanceQuery query);

    ShipmentVolumeForecast forecast(final ForecastQuery query);
}
