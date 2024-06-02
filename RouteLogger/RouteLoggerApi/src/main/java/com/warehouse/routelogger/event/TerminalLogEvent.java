package com.warehouse.routelogger.event;

import com.warehouse.routelogger.RouteLogEvent;
import com.warehouse.routelogger.dto.TerminalLogRequestDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class TerminalLogEvent extends RouteLogBaseEvent implements RouteLogEvent {

    private final TerminalLogRequestDto terminalLogRequest;

    @Builder
    public TerminalLogEvent(@NonNull TerminalLogRequestDto terminalLogRequest) {
        super();
        this.terminalLogRequest = terminalLogRequest;
    }
}
