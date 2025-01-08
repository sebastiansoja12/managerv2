package com.warehouse.routelogger;

public interface RouteLogEventPublisher {
    void send(final RouteLogEvent routeLogEvent);
}
