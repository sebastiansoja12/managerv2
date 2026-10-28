package com.warehouse.incident.application.port.primary.result;

public record IncidentStatistics(long open,
                                 long critical,
                                 long overdue,
                                 long resolvedToday) {
}
