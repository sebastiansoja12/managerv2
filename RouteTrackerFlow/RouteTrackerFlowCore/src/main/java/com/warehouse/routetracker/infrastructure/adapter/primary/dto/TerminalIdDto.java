package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import com.warehouse.commonassets.identificator.TerminalId;

public class TerminalIdDto {
    private Double value;

    public TerminalIdDto() {
    }

    public TerminalIdDto(final Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public static TerminalIdDto from(final TerminalId terminalId) {
        return new TerminalIdDto(terminalId.getValue());
    }
}
