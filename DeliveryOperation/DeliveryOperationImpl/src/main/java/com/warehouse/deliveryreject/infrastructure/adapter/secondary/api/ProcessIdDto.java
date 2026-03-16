package com.warehouse.deliveryreject.infrastructure.adapter.secondary.api;

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
