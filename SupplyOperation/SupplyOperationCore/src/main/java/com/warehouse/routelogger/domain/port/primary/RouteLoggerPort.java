package com.warehouse.routelogger.domain.port.primary;

import com.warehouse.routelogger.domain.model.DeliveryMissedRequest;

public interface RouteLoggerPort {

    void logDeliveryMissed(DeliveryMissedRequest request);
}
