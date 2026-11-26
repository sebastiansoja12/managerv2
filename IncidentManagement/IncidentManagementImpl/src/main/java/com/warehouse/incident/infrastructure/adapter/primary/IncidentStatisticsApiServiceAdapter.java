package com.warehouse.incident.infrastructure.adapter.primary;

import com.warehouse.incident.api.IncidentStatisticsApiService;
import com.warehouse.incident.api.dto.DepartmentIncidentStatisticsDto;
import com.warehouse.incident.application.port.primary.IncidentPort;

import java.time.Instant;
import java.util.List;

public class IncidentStatisticsApiServiceAdapter implements IncidentStatisticsApiService {

    private final IncidentPort incidentPort;

    public IncidentStatisticsApiServiceAdapter(final IncidentPort incidentPort) {
        this.incidentPort = incidentPort;
    }

    @Override
    public List<DepartmentIncidentStatisticsDto> getDepartmentIncidentStatistics(final Instant rangeStart,
                                                                                 final Instant rangeEnd) {
        return this.incidentPort.departmentStatistics(rangeStart, rangeEnd).stream()
                .map(statistics -> new DepartmentIncidentStatisticsDto(statistics.departmentCode(),
                        statistics.damagedShipments(), statistics.damageClaims()))
                .toList();
    }
}
