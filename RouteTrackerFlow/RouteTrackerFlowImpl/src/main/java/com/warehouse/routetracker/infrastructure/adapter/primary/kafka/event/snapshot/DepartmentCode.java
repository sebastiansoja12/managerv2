package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

public record DepartmentCode(String value) {

    public String getValue() {
        return value;
    }
}
