package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.TerminalLogRequest;

public interface RouteLoggerTerminalServicePort {
    void logTerminalId(TerminalLogRequest request);
}
