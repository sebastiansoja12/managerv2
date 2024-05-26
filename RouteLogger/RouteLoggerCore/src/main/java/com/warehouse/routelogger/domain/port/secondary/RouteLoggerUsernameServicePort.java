package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.UsernameLogRequest;

public interface RouteLoggerUsernameServicePort {
    void logUsername(UsernameLogRequest request);
}
