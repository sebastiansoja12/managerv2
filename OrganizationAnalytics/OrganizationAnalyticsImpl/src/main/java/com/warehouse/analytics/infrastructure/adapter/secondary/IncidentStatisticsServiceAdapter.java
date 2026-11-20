package com.warehouse.analytics.infrastructure.adapter.secondary;

import com.warehouse.analytics.application.port.secondary.IncidentStatisticsServicePort;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.analytics.domain.vo.IncidentFigures;
import com.warehouse.incident.api.IncidentStatisticsApiService;

import java.time.ZoneId;
import java.util.List;

public class IncidentStatisticsServiceAdapter implements IncidentStatisticsServicePort {

    private final IncidentStatisticsApiService incidentStatisticsApiService;

    private final ZoneId zoneId;

    public IncidentStatisticsServiceAdapter(final IncidentStatisticsApiService incidentStatisticsApiService,
                                            final ZoneId zoneId) {
        this.incidentStatisticsApiService = incidentStatisticsApiService;
        this.zoneId = zoneId;
    }

    @Override
    public List<IncidentFigures> incidentFigures(final AnalyticsPeriod period) {
        return this.incidentStatisticsApiService.getDepartmentIncidentStatistics(
                        period.dateFrom().atStartOfDay(this.zoneId).toInstant(),
                        period.dateTo().plusDays(1).atStartOfDay(this.zoneId).toInstant())
                .stream()
                .map(statistics -> new IncidentFigures(statistics.departmentCode(), statistics.damagedShipments(),
                        statistics.damageClaims()))
                .toList();
    }
}
