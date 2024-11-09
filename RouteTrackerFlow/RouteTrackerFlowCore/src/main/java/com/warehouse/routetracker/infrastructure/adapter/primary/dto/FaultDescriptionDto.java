package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class FaultDescriptionDto {
    private String value;

    public FaultDescriptionDto() {
    }

    public FaultDescriptionDto(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
