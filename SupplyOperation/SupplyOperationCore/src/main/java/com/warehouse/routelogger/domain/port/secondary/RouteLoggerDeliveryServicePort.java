package com.warehouse.routelogger.domain.port.secondary;

public interface RouteLoggerDeliveryServicePort<T> {
    void logAnyDelivery(T t);
}
