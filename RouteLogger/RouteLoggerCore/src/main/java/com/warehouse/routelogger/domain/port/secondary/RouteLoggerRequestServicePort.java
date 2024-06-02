package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.Request;

public interface RouteLoggerRequestServicePort {
    void logRequest(Request request);
}
