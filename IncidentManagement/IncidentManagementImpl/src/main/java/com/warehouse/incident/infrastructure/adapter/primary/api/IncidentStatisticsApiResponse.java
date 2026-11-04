package com.warehouse.incident.infrastructure.adapter.primary.api;

public record IncidentStatisticsApiResponse(long open,
                                            long critical,
                                            long overdue,
                                            long resolvedToday) {
}
