package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

public class ReturnCodeDto {
    private String value;

    public ReturnCodeDto() {
    }

    public ReturnCodeDto(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
