package com.warehouse.routelogger.domain.port.secondary;

public interface RouteLoggerDeliveryServicePort<T> {
    void logAnyDelivery(T t);

    void logDepotCode(T t);

    void logRequest(T t);

    void logSupplierCode(T t);
}
