package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.VersionLogRequest;

public interface RouteLoggerVersionServicePort {
    void logVersion(VersionLogRequest request);
}
