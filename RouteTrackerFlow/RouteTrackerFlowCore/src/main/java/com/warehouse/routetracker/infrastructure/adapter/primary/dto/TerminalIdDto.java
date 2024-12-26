package com.warehouse.routetracker.infrastructure.adapter.primary.dto;

import com.warehouse.commonassets.identificator.TerminalId;

public class TerminalIdDto {
    private Long value;

    public TerminalIdDto() {
    }

    public TerminalIdDto(final Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static TerminalIdDto from(final TerminalId terminalId) {
        return new TerminalIdDto(terminalId != null ? terminalId.getValue() : null);
    }
}
