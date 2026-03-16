package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import java.util.UUID;


public class ProcessIdDto {
    private UUID value;

    public ProcessIdDto() {
    }

    public ProcessIdDto(final UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }
}
