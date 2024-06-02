package com.warehouse.routelogger.domain.port.secondary;

import com.warehouse.routelogger.domain.model.DepotCodeRequest;

public interface RouteLoggerDepotCodeServicePort {
    void logDepotCode(DepotCodeRequest request);
}
