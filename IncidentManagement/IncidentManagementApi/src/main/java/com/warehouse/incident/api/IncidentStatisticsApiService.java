package com.warehouse.incident.api;

import com.warehouse.incident.api.dto.DepartmentIncidentStatisticsDto;

import java.time.Instant;
import java.util.List;

public interface IncidentStatisticsApiService {

    List<DepartmentIncidentStatisticsDto> getDepartmentIncidentStatistics(final Instant rangeStart,
                                                                          final Instant rangeEnd);
}
