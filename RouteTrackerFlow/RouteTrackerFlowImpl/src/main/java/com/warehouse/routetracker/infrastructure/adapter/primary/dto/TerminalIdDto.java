package com.warehouse.routetracker.infrastructure.adapter.primary.dto;


import com.warehouse.routetracker.domain.vo.TerminalId;

public class TerminalIdDto {
    private String value;

    public TerminalIdDto() {
    }

    public TerminalIdDto(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TerminalIdDto from(final TerminalId terminalId) {
        return new TerminalIdDto(terminalId != null ? terminalId.value() : null);
    }
}
