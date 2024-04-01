package com.warehouse.routelogger.domain.port.primary;

import com.warehouse.routelogger.domain.model.AnyDeliveryRequest;

public interface RouteLoggerPort {

    void logAnyDelivery(AnyDeliveryRequest request);
}
